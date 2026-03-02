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

export function login(username: string, password: string) {
  return http.post<void>('/login', {
    username,
    password,
  })
}

export function logout() {
  return http.post<void>('/logout')
}

export function me() {
  return http.get<User>('/me')
}

