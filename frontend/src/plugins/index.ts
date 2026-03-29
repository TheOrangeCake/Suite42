/**
 * plugins/index.ts
 *
 * Automatically included in `./src/main.ts`
 */

// Types
import type { App } from 'vue'
import router from '../router'
import pinia from '../stores'

export function registerPlugins (app: App) {
  app
    .use(router)
    .use(pinia)
}
