import axios from 'axios';

const api = axios.create({ baseURL: '/api' });

export default {
  list(page = 0, size = 10) {
    return api.get('/books', { params: { page, size } });
  },
  search(params, page = 0, size = 10) {
    return api.post('/books/search', params, { params: { page, size } });
  },
  getById(id) {
    return api.get(`/books/${id}`);
  },
  create(book) {
    return api.post('/books', book);
  },
  update(id, book) {
    return api.put(`/books/${id}`, book);
  },
  delete(id) {
    return api.delete(`/books/${id}`);
  }
};
