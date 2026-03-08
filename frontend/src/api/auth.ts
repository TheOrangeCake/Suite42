import { http } from './http'

export interface User {
  id: number
  username: string
  email: string
  custom_avatar_url: string
  custom_banner_url: string
  first_name: string | null
  last_name: string | null
}

const BASE = '/api/regular-user/v1/regular-user'

export function signup(username: string, email: string, password: string) {
  return http.post<User>(`${BASE}/auth/signup`, { username, email, password })
}

export function signin(login: string, password: string) {
  return http.post<User>(`${BASE}/auth/signin`, { login, password })
}

export function refreshToken() {
  return http.get<User>(`${BASE}/auth/refresh-token`)
}

export function signout() {
  return http.get<void>(`${BASE}/user/signout`)
}
