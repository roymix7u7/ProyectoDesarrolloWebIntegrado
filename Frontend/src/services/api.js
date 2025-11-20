import axios from 'axios';

// Configuración base de la API
const API_BASE_URL = process.env.REACT_APP_API_URL

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptor para añadir el token JWT a todas las peticiones
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Interceptor para manejar errores de respuesta
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Token expirado o inválido
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// ========================
// AUTH ENDPOINTS
// ========================
export const authAPI = {
  registro: (data) => api.post('/auth/registro', data),
  login: (data) => api.post('/auth/login', data),
};

// ========================
// CURSOS ENDPOINTS
// ========================
export const cursosAPI = {
  obtenerTodos: () => api.get('/cursos'),
  obtenerPorId: (id) => api.get(`/cursos/${id}`),
  obtenerPorCategoria: (categoria) => api.get(`/cursos/categoria/${categoria}`),
  crear: (data) => api.post('/cursos', data),
  actualizar: (id, data) => api.put(`/cursos/${id}`, data),
  eliminar: (id) => api.delete(`/cursos/${id}`),
};

// ========================
// INSCRIPCIONES ENDPOINTS
// ========================
export const inscripcionesAPI = {
  obtenerTodas: () => api.get('/inscripciones'),
  obtenerPorId: (id) => api.get(`/inscripciones/${id}`),
  obtenerPorUsuario: (idUsuario) => api.get(`/inscripciones/usuario/${idUsuario}`),
  crear: (data) => api.post('/inscripciones', data),
  actualizarProgreso: (id, progreso) => api.patch(`/inscripciones/${id}/progreso`, null, {
    params: { progreso }
  }),
  eliminar: (id) => api.delete(`/inscripciones/${id}`),
};

export default api;