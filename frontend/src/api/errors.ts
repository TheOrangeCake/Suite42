export class ApiError extends Error {
  constructor(message?: string) {
    super(message)
  }
}

// 401 → pas connecter
export class UnauthenticatedError extends ApiError {}

// 403 → connecter mais interdit
export class ForbiddenError extends ApiError {}

// 404 → ressource inexistante
export class NotFoundError extends ApiError {}

// 422 → donnees invalides
export class ValidationError extends ApiError {
  errors: unknown

  constructor(errors: unknown) {
    super('Validation error')
    this.errors = errors
  }
}

// 500 bug server
export class ServerError extends ApiError {}

