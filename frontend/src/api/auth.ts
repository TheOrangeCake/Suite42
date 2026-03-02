import { http } from './http'

export interface User {
  id: number
  username: string
}


export function signup(username: string, email: string, password: string) {
  return http.post<void>('/api/regular-user/v1/regular-user/auth/signup', {
    username,
    email,
    password,
  })
}

export function login(login: string, password: string) {
  return http.post<void>('/api/regular-user/v1/regular-user/auth/signin', {
    login,
    password,
  })
}

export function logout() {
  return http.post<void>('/logout')
}

export function me() {
  return http.get<User>('/me')
}

