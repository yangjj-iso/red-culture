const { createApp, ref, reactive, computed, watch } = Vue;

createApp({
  setup() {
    const activeTab = ref("home");
    const ui = reactive({ message: "", type: "success", timer: null, loading: false });

    // Auth State
    const user = reactive({
      token: localStorage.getItem("token") || null,
      username: localStorage.getItem("username") || "",
      roles: JSON.parse(localStorage.getItem("roles") || "[]"),
    });

    const showAuthModal = ref(false);
    const authMode = ref("login"); // 'login' or 'register'
    const authForm = reactive({
      username: "",
      password: "",
      confirmPassword: "",
      email: "",
    });

    const showMessage = (message, type = "success") => {
      ui.message = message;
      ui.type = type;
      if (ui.timer) clearTimeout(ui.timer);
      ui.timer = setTimeout(() => {
        ui.message = "";
      }, 3500);
    };

    const api = axios.create({
      headers: { Accept: "application/json" },
      timeout: 15000,
    });

    // Request interceptor to add token
    api.interceptors.request.use((config) => {
      if (user.token) {
        config.headers.Authorization = `Bearer ${user.token}`;
      }
      return config;
    });

    // Response interceptor to handle 401
    api.interceptors.response.use(
      (response) => response,
      (error) => {
        if (error.response && error.response.status === 401) {
          logout();
          showMessage("登录已过期，请重新登录", "error");
        }
        return Promise.reject(error);
      }
    );

    const toggleAuthMode = () => {
      authMode.value = authMode.value === "login" ? "register" : "login";
      authForm.username = "";
      authForm.password = "";
      authForm.confirmPassword = "";
      authForm.email = "";
    };

    const handleAuth = async () => {
      if (!authForm.username || !authForm.password) {
        return showMessage("请输入用户名和密码", "error");
      }

      if (authMode.value === "register") {
        if (authForm.password !== authForm.confirmPassword) {
          return showMessage("两次输入的密码不一致", "error");
        }
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (authForm.email && !emailRegex.test(authForm.email)) {
             return showMessage("请输入有效的邮箱地址", "error");
        }
        try {
          await api.post("/api/auth/signup", {
            username: authForm.username,
            password: authForm.password,
            email: authForm.email,
            role: ["user"], // Default role
          });
          showMessage("注册成功，请登录", "success");
          authMode.value = "login";
        } catch (e) {
          showMessage(normalizeError(e), "error");
        }
      } else {
        try {
          const res = await api.post("/api/auth/signin", {
            username: authForm.username,
            password: authForm.password,
          });
          
          user.token = res.data.token;
          user.username = res.data.username;
          user.roles = res.data.roles;
          
          localStorage.setItem("token", user.token);
          localStorage.setItem("username", user.username);
          localStorage.setItem("roles", JSON.stringify(user.roles));
          localStorage.setItem("userId", res.data.id);
          
          showAuthModal.value = false;
          showMessage("登录成功", "success");
          setTab("dashboard");
        } catch (e) {
          showMessage(normalizeError(e), "error");
        }
      }
    };

    const logout = () => {
      user.token = null;
      user.username = "";
      user.roles = [];
      localStorage.removeItem("token");
      localStorage.removeItem("username");
      localStorage.removeItem("roles");
      localStorage.removeItem("userId");
      showMessage("已退出登录", "success");
      setTab("dashboard");
    };


    const normalizeError = (error) => {
      const resp = error?.response;
      if (resp?.data?.message) return resp.data.message;
      if (resp?.data?.error && resp?.data?.path) return `${resp.data.error} ${resp.data.path}`;
      if (resp?.status) return `请求失败 (${resp.status})`;
      return "请求失败";
    };

    const request = async (fn, successMessage) => {
      ui.loading = true;
      try {
        const res = await fn();
        if (successMessage) showMessage(successMessage, "success");
        return res;
      } catch (e) {
        showMessage(normalizeError(e), "error");
        throw e;
      } finally {
        ui.loading = false;
      }
    };

    const options = reactive({
      redSpots: [],
      organizations: [],
      visitActivities: [],
      redSpotNameById: {},
      orgNameById: {},
    });

    const rebuildOptionMaps = () => {
      options.redSpotNameById = Object.fromEntries(options.redSpots.map((s) => [s.id, s.name]));
      options.orgNameById = Object.fromEntries(options.organizations.map((o) => [o.id, o.name]));
    };

    const loadRedSpotsOption = async () => {
      const res = await request(() => api.get("/api/red-spots"));
      options.redSpots = res.data || [];
      rebuildOptionMaps();
    };

    const loadOrganizationsOption = async () => {
      const res = await request(() => api.get("/api/organizations"));
      options.organizations = res.data || [];
      rebuildOptionMaps();
    };

    const loadVisitActivitiesOption = async () => {
      const res = await request(() => api.get("/api/visit-activities"));
      options.visitActivities = res.data || [];
    };

    const dash = reactive({
      spotNameQuery: "黄麻起义和鄂豫皖苏区纪念园",
      spotEvents: [],
      spotEventsSearched: false,
      startDate: "2023-01-01",
      endDate: "2023-12-31",
      visitsCount: [],
      visitsSearched: false,
      ratingQuery: "5",
      feedbackList: [],
      feedbackSearched: false,
      figureNameQuery: "李先念",
      resourcesList: [],
      resourcesSearched: false,
      schoolVisits: [],
      searchSpotEvents: async () => {
        if (!dash.spotNameQuery) return showMessage("请输入景点名称", "error");
        const res = await request(() => api.get("/api/statistics/spot-events", { params: { spotName: dash.spotNameQuery } }));
        dash.spotEvents = res.data || [];
        dash.spotEventsSearched = true;
      },
      searchVisits: async () => {
        if (!dash.startDate || !dash.endDate) return showMessage("请选择日期范围", "error");
        const res = await request(() =>
          api.get("/api/statistics/visits-count", { params: { startDate: dash.startDate, endDate: dash.endDate } })
        );
        dash.visitsCount = res.data || [];
        dash.visitsSearched = true;
      },
      searchFeedback: async () => {
        const res = await request(() => api.get("/api/statistics/feedback-by-rating", { params: { rating: dash.ratingQuery } }));
        dash.feedbackList = res.data || [];
        dash.feedbackSearched = true;
      },
      searchResources: async () => {
        if (!dash.figureNameQuery) return showMessage("请输入人物姓名", "error");
        const res = await request(() =>
          api.get("/api/statistics/resources-by-figure", { params: { figureName: dash.figureNameQuery } })
        );
        dash.resourcesList = res.data || [];
        dash.resourcesSearched = true;
      },
      loadSchoolVisits: async () => {
        const res = await request(() => api.get("/api/statistics/school-visits-national"));
        dash.schoolVisits = res.data || [];
      },
    });

    const spots = reactive({
      items: [],
      editingId: null,
      form: {
        name: "",
        address: "",
        protectionLevel: "",
        historyBackground: "",
        longitude: "",
        latitude: "",
      },
      resetForm() {
        spots.form = {
          name: "",
          address: "",
          protectionLevel: "",
          historyBackground: "",
          longitude: "",
          latitude: "",
        };
      },
      async load() {
        const res = await request(() => api.get("/api/red-spots"));
        spots.items = res.data || [];
        options.redSpots = spots.items.slice();
        rebuildOptionMaps();
      },
      startEdit(item) {
        spots.editingId = item.id;
        spots.form = {
          name: item.name || "",
          address: item.address || "",
          protectionLevel: item.protectionLevel || "",
          historyBackground: item.historyBackground || "",
          longitude: item.longitude ?? "",
          latitude: item.latitude ?? "",
        };
      },
      cancelEdit() {
        spots.editingId = null;
        spots.resetForm();
      },
      async create() {
        if (!spots.form.name) return showMessage("请填写景点名称", "error");
        const payload = {
          name: spots.form.name,
          address: spots.form.address || null,
          protectionLevel: spots.form.protectionLevel || null,
          historyBackground: spots.form.historyBackground || null,
          longitude: spots.form.longitude === "" ? null : spots.form.longitude,
          latitude: spots.form.latitude === "" ? null : spots.form.latitude,
        };
        await request(() => api.post("/api/red-spots", payload), "新增成功");
        await spots.load();
        spots.resetForm();
      },
      async update() {
        if (spots.editingId == null) return;
        const payload = {
          name: spots.form.name,
          address: spots.form.address || null,
          protectionLevel: spots.form.protectionLevel || null,
          historyBackground: spots.form.historyBackground || null,
          longitude: spots.form.longitude === "" ? null : spots.form.longitude,
          latitude: spots.form.latitude === "" ? null : spots.form.latitude,
        };
        await request(() => api.put(`/api/red-spots/${spots.editingId}`, payload), "修改成功");
        spots.editingId = null;
        spots.resetForm();
        await spots.load();
      },
      async remove(id) {
        await request(() => api.delete(`/api/red-spots/${id}`), "删除成功");
        await spots.load();
      },
    });

    const figures = reactive({
      items: [],
      editingId: null,
      form: { name: "", hometown: "", birthDate: "", deathDate: "", biography: "" },
      resetForm() {
        figures.form = { name: "", hometown: "", birthDate: "", deathDate: "", biography: "" };
      },
      async load() {
        const res = await request(() => api.get("/api/historical-figures"));
        figures.items = res.data || [];
      },
      startEdit(item) {
        figures.editingId = item.id;
        figures.form = {
          name: item.name || "",
          hometown: item.hometown || "",
          birthDate: item.birthDate || "",
          deathDate: item.deathDate || "",
          biography: item.biography || "",
        };
      },
      cancelEdit() {
        figures.editingId = null;
        figures.resetForm();
      },
      async create() {
        if (!figures.form.name) return showMessage("请填写姓名", "error");
        const payload = {
          name: figures.form.name,
          hometown: figures.form.hometown || null,
          birthDate: figures.form.birthDate || null,
          deathDate: figures.form.deathDate || null,
          biography: figures.form.biography || null,
        };
        await request(() => api.post("/api/historical-figures", payload), "新增成功");
        await figures.load();
        figures.resetForm();
      },
      async update() {
        if (figures.editingId == null) return;
        const payload = {
          name: figures.form.name,
          hometown: figures.form.hometown || null,
          birthDate: figures.form.birthDate || null,
          deathDate: figures.form.deathDate || null,
          biography: figures.form.biography || null,
        };
        await request(() => api.put(`/api/historical-figures/${figures.editingId}`, payload), "修改成功");
        figures.editingId = null;
        figures.resetForm();
        await figures.load();
      },
      async remove(id) {
        await request(() => api.delete(`/api/historical-figures/${id}`), "删除成功");
        await figures.load();
      },
    });

    const events = reactive({
      items: [],
      editingId: null,
      form: { title: "", eventDate: "", description: "", redSpotId: null },
      resetForm() {
        events.form = { title: "", eventDate: "", description: "", redSpotId: null };
      },
      async load() {
        await loadRedSpotsOption();
        const res = await request(() => api.get("/api/historical-events"));
        events.items = res.data || [];
      },
      startEdit(item) {
        events.editingId = item.id;
        events.form = {
          title: item.title || "",
          eventDate: item.eventDate || "",
          description: item.description || "",
          redSpotId: item.redSpotId ?? null,
        };
      },
      cancelEdit() {
        events.editingId = null;
        events.resetForm();
      },
      async create() {
        if (!events.form.title) return showMessage("请填写事件标题", "error");
        const payload = {
          title: events.form.title,
          eventDate: events.form.eventDate || null,
          description: events.form.description || null,
          redSpotId: events.form.redSpotId ?? null,
        };
        await request(() => api.post("/api/historical-events", payload), "新增成功");
        await events.load();
        events.resetForm();
      },
      async update() {
        if (events.editingId == null) return;
        const payload = {
          title: events.form.title,
          eventDate: events.form.eventDate || null,
          description: events.form.description || null,
          redSpotId: events.form.redSpotId ?? null,
        };
        await request(() => api.put(`/api/historical-events/${events.editingId}`, payload), "修改成功");
        events.editingId = null;
        events.resetForm();
        await events.load();
      },
      async remove(id) {
        await request(() => api.delete(`/api/historical-events/${id}`), "删除成功");
        await events.load();
      },
    });

    const resources = reactive({
      items: [],
      editingId: null,
      form: { title: "", resourceType: "Book", contentUrl: "", description: "", publishDate: "", redSpotId: null },
      resetForm() {
        resources.form = { title: "", resourceType: "Book", contentUrl: "", description: "", publishDate: "", redSpotId: null };
      },
      async load() {
        await loadRedSpotsOption();
        const res = await request(() => api.get("/api/education-resources"));
        resources.items = res.data || [];
      },
      startEdit(item) {
        resources.editingId = item.id;
        resources.form = {
          title: item.title || "",
          resourceType: item.resourceType || "Book",
          contentUrl: item.contentUrl || "",
          description: item.description || "",
          publishDate: item.publishDate || "",
          redSpotId: item.redSpotId ?? null,
        };
      },
      cancelEdit() {
        resources.editingId = null;
        resources.resetForm();
      },
      async create() {
        if (!resources.form.title) return showMessage("请填写资源标题", "error");
        const payload = {
          title: resources.form.title,
          resourceType: resources.form.resourceType,
          contentUrl: resources.form.contentUrl || null,
          description: resources.form.description || null,
          publishDate: resources.form.publishDate || null,
          redSpotId: resources.form.redSpotId ?? null,
        };
        await request(() => api.post("/api/education-resources", payload), "新增成功");
        await resources.load();
        resources.resetForm();
      },
      async update() {
        if (resources.editingId == null) return;
        const payload = {
          title: resources.form.title,
          resourceType: resources.form.resourceType,
          contentUrl: resources.form.contentUrl || null,
          description: resources.form.description || null,
          publishDate: resources.form.publishDate || null,
          redSpotId: resources.form.redSpotId ?? null,
        };
        await request(() => api.put(`/api/education-resources/${resources.editingId}`, payload), "修改成功");
        resources.editingId = null;
        resources.resetForm();
        await resources.load();
      },
      async remove(id) {
        await request(() => api.delete(`/api/education-resources/${id}`), "删除成功");
        await resources.load();
      },
    });

    const orgs = reactive({
      items: [],
      editingId: null,
      form: { name: "", contactPerson: "", phone: "", orgType: "School" },
      resetForm() {
        orgs.form = { name: "", contactPerson: "", phone: "", orgType: "School" };
      },
      async load() {
        const res = await request(() => api.get("/api/organizations"));
        orgs.items = res.data || [];
        options.organizations = orgs.items.slice();
        rebuildOptionMaps();
      },
      startEdit(item) {
        orgs.editingId = item.id;
        orgs.form = {
          name: item.name || "",
          contactPerson: item.contactPerson || "",
          phone: item.phone || "",
          orgType: item.orgType || "School",
        };
      },
      cancelEdit() {
        orgs.editingId = null;
        orgs.resetForm();
      },
      async create() {
        if (!orgs.form.name) return showMessage("请填写单位名称", "error");
        if (orgs.form.phone && !/^\d{7,15}$/.test(orgs.form.phone)) {
            return showMessage("请输入有效的电话号码", "error");
        }
        const payload = {
          name: orgs.form.name,
          contactPerson: orgs.form.contactPerson || null,
          phone: orgs.form.phone || null,
          orgType: orgs.form.orgType || null,
        };
        await request(() => api.post("/api/organizations", payload), "新增成功");
        await orgs.load();
        orgs.resetForm();
      },
      async update() {
        if (orgs.editingId == null) return;
        const payload = {
          name: orgs.form.name,
          contactPerson: orgs.form.contactPerson || null,
          phone: orgs.form.phone || null,
          orgType: orgs.form.orgType || null,
        };
        await request(() => api.put(`/api/organizations/${orgs.editingId}`, payload), "修改成功");
        orgs.editingId = null;
        orgs.resetForm();
        await orgs.load();
      },
      async remove(id) {
        await request(() => api.delete(`/api/organizations/${id}`), "删除成功");
        await orgs.load();
      },
    });

    const visits = reactive({
      items: [],
      editingId: null,
      showForm: false,
      viewMode: 'my', // 'my' or 'all'
      form: { theme: "", visitTime: "", participantCount: 1, status: "Pending", organizationId: null, redSpotId: null },
      resetForm() {
        visits.form = { theme: "", visitTime: "", participantCount: 1, status: "Pending", organizationId: null, redSpotId: null };
        visits.showForm = false;
      },
      async load() {
        // Default load based on role
        if (user.roles.includes('ROLE_ADMIN') || user.roles.includes('ROLE_EDITOR')) {
            visits.viewMode = 'all';
            await visits.loadAll();
        } else {
            visits.viewMode = 'my';
            await visits.loadMyReservations();
        }
      },
      async loadAll() {
        await Promise.all([loadRedSpotsOption(), loadOrganizationsOption()]);
        const res = await request(() => api.get("/api/visit-activities"));
        visits.items = res.data || [];
        options.visitActivities = visits.items.slice();
      },
      async loadMyReservations() {
        if (!user.token) {
            visits.items = [];
            return;
        }
        await Promise.all([loadRedSpotsOption(), loadOrganizationsOption()]);
        const userId = localStorage.getItem("userId");
        if (!userId) return;
        
        const res = await request(() => api.get("/api/visit-activities/my", { params: { userId: userId } }));
        visits.items = res.data || [];
      },
      async switchView(mode) {
          visits.viewMode = mode;
          if (mode === 'all') await visits.loadAll();
          else await visits.loadMyReservations();
      },
      async refreshList() {
          if (visits.viewMode === 'all') await visits.loadAll();
          else await visits.loadMyReservations();
      },
      async reserve() {
         if (!visits.form.theme) return showMessage("请填写活动主题", "error");
         if (!visits.form.visitTime) return showMessage("请选择参观时间", "error");
         if (visits.form.redSpotId == null) return showMessage("请选择参观景点", "error");
         
         const userId = localStorage.getItem("userId");
         if (!userId) return showMessage("请重新登录", "error");

         const payload = {
            theme: visits.form.theme,
            visitTime: visits.form.visitTime,
            participantCount: visits.form.participantCount,
            status: "Pending",
            organizationId: visits.form.organizationId,
            redSpotId: visits.form.redSpotId,
            userId: parseInt(userId)
         };
         await request(() => api.post("/api/visit-activities/reserve", payload), "预约提交成功，请等待审核");
         
         visits.resetForm();
         // Switch to 'my' view to see the new reservation
         visits.viewMode = 'my';
         await visits.loadMyReservations();
      },
      async audit(id, status) {
          await request(() => api.put(`/api/visit-activities/${id}/audit`, null, { params: { status: status } }), "操作成功");
          await visits.refreshList();
      },
      startEdit(item) {
        visits.editingId = item.id;
        visits.form = {
          theme: item.theme || "",
          visitTime: item.visitTime || "",
          participantCount: item.participantCount ?? 0,
          status: item.status || "Reserved",
          organizationId: item.organizationId ?? null,
          redSpotId: item.redSpotId ?? null,
        };
      },
      cancelEdit() {
        visits.editingId = null;
        visits.resetForm();
      },
      async create() {
         // Keep existing admin create for compatibility if needed, or redirect to reserve
         // But for admin create, we might want to specify status directly.
         // Let's reuse reserve but maybe allow status override if we were fully implementing admin create.
         // For now, let's just use the reserve flow even for admins but maybe with auto-approve?
         // Or just keep the old create logic?
         // The user asked for "appointment function", so reserve is key.
         // Let's stick to the new reserve function for creation.
         await visits.reserve();
      },
      async update() {
        if (visits.editingId == null) return;
        const payload = {
          theme: visits.form.theme,
          visitTime: visits.form.visitTime || null,
          participantCount: visits.form.participantCount,
          status: visits.form.status,
          organizationId: visits.form.organizationId,
          redSpotId: visits.form.redSpotId
        };
        await request(() => api.put(`/api/visit-activities/${visits.editingId}`, payload), "修改成功");
        visits.editingId = null;
        visits.resetForm();
        await visits.load();
      },
      async remove(id) {
        await request(() => api.delete(`/api/visit-activities/${id}`), "删除成功");
        await visits.load();
      },
    });

    const feedback = reactive({
      items: [],
      editingId: null,
      form: { visitorName: "", content: "", rating: 5, visitActivityId: null },
      resetForm() {
        feedback.form = { visitorName: "", content: "", rating: 5, visitActivityId: null };
      },
      async load() {
        await loadVisitActivitiesOption();
        const res = await request(() => api.get("/api/visitor-feedback"));
        feedback.items = res.data || [];
      },
      startEdit(item) {
        feedback.editingId = item.id;
        feedback.form = {
          visitorName: item.visitorName || "",
          content: item.content || "",
          rating: item.rating ?? 5,
          visitActivityId: item.visitActivityId ?? null,
        };
      },
      cancelEdit() {
        feedback.editingId = null;
        feedback.resetForm();
      },
      async create() {
        if (!feedback.form.content) return showMessage("请填写反馈内容", "error");
        if (feedback.form.visitActivityId == null) return showMessage("请选择关联活动", "error");
        const payload = {
          visitorName: feedback.form.visitorName || null,
          content: feedback.form.content,
          rating: feedback.form.rating,
          visitActivityId: feedback.form.visitActivityId,
        };
        await request(() => api.post("/api/visitor-feedback", payload), "新增成功");
        await feedback.load();
        feedback.resetForm();
      },
      async update() {
        if (feedback.editingId == null) return;
        const payload = {
          visitorName: feedback.form.visitorName || null,
          content: feedback.form.content,
          rating: feedback.form.rating,
          visitActivityId: feedback.form.visitActivityId ?? null,
        };
        await request(() => api.put(`/api/visitor-feedback/${feedback.editingId}`, payload), "修改成功");
        feedback.editingId = null;
        feedback.resetForm();
        await feedback.load();
      },
      async remove(id) {
        await request(() => api.delete(`/api/visitor-feedback/${id}`), "删除成功");
        await feedback.load();
      },
    });

    const users = reactive({
      items: [],
      currentUserId: localStorage.getItem("userId") || null,
      async load() {
        const res = await request(() => api.get("/api/users"));
        users.items = (res.data || []).map(u => ({
          ...u,
          selectedRole: u.roles && u.roles[0] && u.roles[0].name || 'ROLE_USER'
        }));
      },
      async changeRole(userId, newRole) {
        await request(() => api.put(`/api/users/${userId}/role`, null, { params: { role: newRole } }), "角色修改成功");
        await users.load();
      },
      async deleteUser(userId) {
        if (!confirm("确定要删除该用户吗？此操作不可恢复！")) return;
        await request(() => api.delete(`/api/users/${userId}`), "用户删除成功");
        await users.load();
      },
    });

    const getRoleDisplayName = (role) => {
      const roleMap = {
        'ROLE_ADMIN': '管理员',
        'ROLE_EDITOR': '编辑',
        'ROLE_USER': '普通用户',
        'ROLE_VISITOR': '访客'
      };
      return roleMap[role] || role || '未知';
    };

    const setTab = async (tab) => {
      activeTab.value = tab;
    };

    watch(
      activeTab,
      async (tab) => {
        if (tab === "spots") await spots.load();
        if (tab === "figures") await figures.load();
        if (tab === "events") await events.load();
        if (tab === "resources") await resources.load();
        if (tab === "orgs") await orgs.load();
        if (tab === "visits") await visits.load();
        if (tab === "feedback") await feedback.load();
        if (tab === "users") await users.load();
      },
      { immediate: true }
    );

    return {
      activeTab,
      ui,
      user,
      showAuthModal,
      authMode,
      authForm,
      toggleAuthMode,
      handleAuth,
      logout,
      setTab,
      dash,
      spots,
      figures,
      events,
      resources,
      orgs,
      visits,
      feedback,
      users,
      getRoleDisplayName,
      options,
    };
  },
}).mount("#app");
