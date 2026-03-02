/**
 * main.ts
 *
 * Bootstraps Vuetify and other plugins then mounts the App`
 */

import '@/styles/themes.css'
import "tailwindcss";

import { createPinia } from 'pinia'
// Plugins
import { registerPlugins } from '@/plugins'

// Components
import App from './App.vue'

// Composables
import { createApp } from 'vue'

// Styles
import 'unfonts.css'
import '@/styles/tailwind.css'

const app = createApp(App)

app.use(createPinia())
registerPlugins(app)

app.mount('#app')
