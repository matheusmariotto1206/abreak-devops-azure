// Módulo de comunicação com a API
const API = {
    // Helper para fazer requisições HTTP
    async request(endpoint, options = {}) {
        const url = buildUrl(endpoint);
        
        const defaultOptions = {
            headers: {
                'Content-Type': 'application/json',
            }
        };
        
        const config = { ...defaultOptions, ...options };
        
        try {
            const response = await fetch(url, config);
            
            // Se 204 No Content, retornar null
            if (response.status === 204) {
                return null;
            }
            
            const data = await response.json();
            
            if (!response.ok) {
                throw new Error(data.message || `Erro ${response.status}`);
            }
            
            return data;
        } catch (error) {
            console.error('❌ Erro na requisição:', error);
            throw error;
        }
    },
    
    // ========== USUÁRIOS ==========
    
    async createUser(userData) {
        return this.request(CONFIG.ENDPOINTS.USERS, {
            method: 'POST',
            body: JSON.stringify(userData)
        });
    },
    
    async getUserById(userId) {
    return this.request(`${CONFIG.ENDPOINTS.USERS}/${userId}`, {
        method: 'GET'
    });
},
    
    async getAllUsers(page = 0, size = 100) {
    return this.request(CONFIG.ENDPOINTS.USERS + `?page=${page}&size=${size}`);
   },
    
    // ========== PAUSAS ==========
    
    async createBreak(breakData) {
        return this.request(CONFIG.ENDPOINTS.BREAKS, {
            method: 'POST',
            body: JSON.stringify(breakData)
        });
    },
    
    async updateBreak(breakId, breakData) {
        return this.request(`${CONFIG.ENDPOINTS.BREAKS}/${breakId}`, {
            method: 'PUT',
            body: JSON.stringify(breakData)
        });
    },
    
    async deleteBreak(breakId) {
        return this.request(`${CONFIG.ENDPOINTS.BREAKS}/${breakId}`, {
            method: 'DELETE'
        });
    },
    
    async getBreaksToday(userId) {
        return this.request(`${CONFIG.ENDPOINTS.BREAKS}/today/${userId}`);
    },
    
    async getUserBreaks(userId, page = 0, size = 20) {
    return this.request(`${CONFIG.ENDPOINTS.BREAKS}/user/${userId}?page=${page}&size=${size}`);
}
};

window.API = API;