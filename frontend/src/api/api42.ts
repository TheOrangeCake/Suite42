import { http } from './http'

export interface IndividualProject {
  cursus_project_id: number
  name: string
  slug: string
  projects_users: {
    occurrence: number
    final_mark: number | null
    status: string
    validated: boolean | null
  } | null
}

export interface IndividualRank {
  rank: number
  projects: IndividualProject[]
}

export interface UserDetailed {
  id: number
  campus: string
  email: string
  login: string
  first_name: string
  last_name: string
  intra_url: string
  custom_avatar_url: string | null
  custom_banner_url: string | null
  image: {
    link: string | null
    versions: {
      large: string | null
      medium: string | null
      small: string | null
      micro: string | null
    }
  }
  pool_month: string | null
  pool_year: string | null
  alumni: boolean
  active: boolean
  rank: number
  rank_progress_percent: number
  performance_score: number
  lfg: string | null
  finished_projects: string[]
  eligible_projects: string[]
  common_core: {
    rank: IndividualRank[]
  }
}

export interface User42 {
  id: number
  login: string
  first_name: string
  last_name: string
  intra_url: string
  custom_avatar_url: string | null
  image: {
    versions: {
      small: string | null
      medium: string | null
    }
  }
  pool_month: string | null
  pool_year: string | null
  rank: number
  rank_progress_percent: number
  lfg: string | null
}

export interface PagedResponse<T> {
  content: T[]
  page: {
    size: number
    number: number
    totalElements: number
    totalPages: number
  }
}

export interface FinderFilters {
  campusName: string
  page: number
  size: number
  rank?: number
  poolYear?: string
  poolMonth?: string
  eligibleProject?: string
  lfg?: boolean
  sort?: string
}

export function getUsers(filters: FinderFilters) {
  const params: Record<string, string> = {
    campusName: filters.campusName,
    page: filters.page.toString(),
    size: filters.size.toString(),
  }
  if (filters.rank !== undefined) params.rank = filters.rank.toString()
  if (filters.poolYear) params.poolYear = filters.poolYear
  if (filters.poolMonth) params.poolMonth = filters.poolMonth
  if (filters.eligibleProject) params.eligibleProject = filters.eligibleProject
  if (filters.lfg !== undefined) params.lfg = filters.lfg.toString()
  if (filters.sort) params.sort = filters.sort

  return http.get<PagedResponse<User42>>('/api/api42/v1/42users', params)
}

export function getUserProfile() {
  return http.get<UserDetailed>('/api/api42/v1/42users/profile')
}

export function getUserByLogin(login: string) {
  return http.get<UserDetailed>(`/api/api42/v1/42users/profile/login/${login}`)
}
