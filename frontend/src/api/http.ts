import {
  UnauthenticatedError,
  ForbiddenError,
  NotFoundError,
  ValidationError,
  ServerError,
} from './errors'

const API_BASE_URL = import.meta.env.VITE_API_URL

async function request<T>(
  url: string,
  options: RequestInit = {}
): Promise<T> {

  const response = await fetch(`${API_BASE_URL}${url}`, {
    credentials: 'include', //  ENVOIE LES COOKIES HttpOnly
    headers: {
      'Content-Type': 'application/json',
      ...options.headers,
    },
    ...options,
  })

  // Succes
  if (response.ok) {
    // 204 = No Content
    if (response.status === 204) {
      return null as T
    }
    return response.json()
  }

  // Erreurs normalisees
  if (response.status === 401) {
    throw new UnauthenticatedError()
  }

  if (response.status === 403) {
    throw new ForbiddenError()
  }

  if (response.status === 404) {
    throw new NotFoundError()
  }

  if (response.status === 422) {
    const data = await response.json()
    throw new ValidationError(data)
  }

  throw new ServerError()
}

// API publique
export const http = {
  get<T>(url: string, params?: Record<string, string>) {
    const query = params ? `?${new URLSearchParams(params)}` : ''
    return request<T>(`${url}${query}`)
  },

  post<T>(url: string, body?: unknown) {
    return request<T>(url, {
      method: 'POST',
      body: JSON.stringify(body),
    })
  },

  put<T>(url: string, body?: unknown) {
    return request<T>(url, {
      method: 'PUT',
      body: JSON.stringify(body),
    })
  },

  delete<T>(url: string) {
    return request<T>(url, {
      method: 'DELETE',
    })
  },
}

