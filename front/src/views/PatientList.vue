<template>
  <div class="content-wrapper">
    <h2>Listes des patients</h2>

    <ul v-if="patients.length">
      <li v-for="p in patients" :key="p.id" style="margin: 1em 0;">
        👤 {{ p.firstName }} {{ p.lastName }} ({{ p.gender }}, {{ p.dateOfBirth }})
        <router-link :to="`/patient/${p.id}`">👁️ Voir</router-link>
        <button @click="deletePatient(p.id)">🗑️ Supprimer</button>
      </li>
    </ul>
  </div>

</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const patients = ref([])
const patientMessage = ref('')

const fetchPatients = async () => {
  try {
    const res = await axios.get('http://localhost:8080/patient')
    patients.value = res.data
  } catch (e) {
    console.error('❌ Erreur fetchPatients:', e)
  }
}

const deletePatient = async (id) => {
  if (!confirm('❗ Supprimer ce patient ?')) return
  try {
    await axios.delete(`http://localhost:8080/patient/${id}`)
    await fetchPatients()
    patientMessage.value = '✅ Patient supprimé.'
  } catch (e) {
    patientMessage.value = '❌ Erreur suppression.'
  }
}

onMounted(fetchPatients)
</script>

<style scoped>
.content-wrapper {
  background-color: white;
  padding: 2rem;
  margin: 2rem auto; /* <- centrer horizontalement */
  border-radius: 10px;
  max-width: 800px;
  width: 100%;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.1);
}
</style>



