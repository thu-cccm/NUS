import { fileURLToPath, URL } from "url";

import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      "@": fileURLToPath(new URL("./src", import.meta.url)),
    },
  },

  server: {
    host: '0.0.0.0',
    port: '3000',
    open: false, 
    hmr: true, 
    proxy: {
      '/api': {

        target: 'http:

        changeOrigin: true,

        rewrite: path => path.replace(/^\/api/, ''),
      },
      '/wx': {
        target: 'https:
        changeOrigin: true,
        rewrite: path => path.replace(/^\/wx/, ''),
      },
    },
  },

});
