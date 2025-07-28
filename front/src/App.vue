<template>
  <div id="app">
    <header class="app-header">
      <h1>MediLabo 🩺</h1>
      <nav>
        <router-link to="/">👥 Liste des patients</router-link>
        <router-link to="/add">➕ Ajouter un patient</router-link>
        <router-link to="/note">📝 Ajouter une note</router-link>
        <a href="#" @click.prevent="logout" class="nav-link">🚪 Logout</a>
      </nav>
    </header>

    <main class="main-content">
      <div class="content-container">
        <router-view />
      </div>
    </main>
  </div>
</template>

<style scoped>
:global(body),
:global(html) {
  margin: 0;
  padding: 0;
  background-color: white;
}

#app {
  margin: 0;
  padding: 0;
  font-family: Arial, sans-serif;
  background: white;
  min-height: 100vh;
}

.app-header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  background-color: #007bff;
  color: white;
  padding: 1rem 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  z-index: 1000;
}

.app-header h1 {
  margin: 0;
}

.app-header nav a {
  margin-left: 1.5rem;
  color: white;
  text-decoration: none;
  font-weight: bold;
}

.app-header nav a:hover {
  text-decoration: underline;
}

.main-content {
  display: flex;
  justify-content: center;
  align-items: center;
  padding-top: 12px;
  padding-bottom: 2rem;
  background-color: white;
  height: 80vh;
  width: 125vh;
  box-sizing: border-box;
}

.content-container {
  width: 100%;
  max-width: 800px;
  padding: 0 1rem;
  display: flex;
  justify-content: center;
  color: #1e1e1e;
}
</style>

<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

const logout = () => {
  fetch('/logout', {
    method: 'POST',
    credentials: 'include',
  })
      .then(() => {
        window.location.href = '/login'
      })
      .catch((e) => {
        console.error('Erreur logout:', e)
      })
}
</script>
