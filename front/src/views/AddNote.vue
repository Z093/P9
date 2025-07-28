<template>
  <div class="content-wrapper">
    <h2 style="margin-bottom: 1em;">🗒️ Ajouter une note</h2>
    <form @submit.prevent="addNote" style="display: flex; flex-direction: column; gap: 1em;">
      <div>
        <label>Patient</label><br />
        <select v-model="note.patientId" required style="width: 100%; padding: 0.5em;">
          <option disabled value="">Choisir un patient</option>
          <option v-for="p in patients" :key="p.id" :value="p.id">
            {{ p.firstName }} {{ p.lastName }}
          </option>
        </select>
      </div>

      <div>
        <label>Contenu de la note</label><br />
        <textarea
            v-model="note.content"
            placeholder="Saisir les détails ici..."
            required
            rows="5"
            style="width: 100%; padding: 0.5em; resize: vertical;"
        ></textarea>
      </div>

      <button type="submit" style="padding: 0.6em; background-color: #28a745; color: white; border: none; border-radius: 4px; cursor: pointer;">
        ➕ Ajouter Note
      </button>
    </form>

    <p v-if="noteMessage" style="color: green; font-weight: bold; margin-top: 1em;">{{ noteMessage }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const patients = ref([])
const note = ref({ patientId: '', content: '' })
const noteMessage = ref('')

const fetchPatients = async () => {
  try {
    const res = await axios.get('http://localhost:8080/patient')
    patients.value = res.data
  } catch (e) {
    console.error('❌ Erreur chargement patients:', e)
  }
}

const addNote = async () => {
  try {
    await axios.post('http://localhost:8080/notes', note.value)
    noteMessage.value = '✅ Note ajoutée avec succès.'
    note.value.patientId = ''
    note.value.content = ''
  } catch (e) {
    console.error('❌ Erreur ajout note:', e)
    noteMessage.value = '❌ Échec de l\'ajout de la note.'
  }
}

onMounted(fetchPatients)
</script>

<style scoped>
.content-wrapper {
  background-color: #ffffff;
  border-radius: 10px;
  padding: 2rem;
  max-width: 700px;
  width: 100%;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
}
</style>
