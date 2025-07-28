<template>
  <div class="content-container">
    <div class="details-wrapper" v-if="patient">
      <h2>🧾 Détails du patient</h2>
      <ul>
        <li><strong>ID :</strong> {{ patient.id }}</li>
        <li><strong>Prénom :</strong> {{ patient.firstName }}</li>
        <li><strong>Nom :</strong> {{ patient.lastName }}</li>
        <li><strong>Date de naissance :</strong> {{ patient.dateOfBirth }}</li>
        <li><strong>Sexe :</strong> {{ patient.gender === 'M' ? 'Homme' : 'Femme' }}</li>
        <li><strong>Téléphone :</strong> {{ patient.phoneNumber }}</li>
        <li><strong>Adresse :</strong> {{ patient.address }}</li>
      </ul>

      <button @click="editingPatient = { ...patient }">✏️ Modifier le patient</button>

      <div class="risk-box" v-if="riskLevel">
        <h3>🧪 Évaluation du risque :</h3>
        <p><strong>Résultat :</strong> {{ riskLevel }}</p>
      </div>

      <div class="notes-box">
        <h3>📝 Notes médicales :</h3>
        <ul v-if="notes.length">
          <li v-for="note in notes" :key="note.id">
            <div v-if="editingNote && editingNote.id === note.id">
              <textarea v-model="editingNote.content"></textarea>
              <button @click="updateNote()">💾 Sauvegarder</button>
              <button @click="cancelEditNote">❌ Annuler</button>
            </div>
            <div v-else>
              <p>{{ note.content }}</p>
              <button @click="startEditNote(note)">✏️ Modifier</button>
              <button @click="deleteNote(note.id)">🗑️ Supprimer</button>
            </div>
          </li>
        </ul>
      </div>
    </div>

    <div v-if="editingPatient" class="edit-form">
      <h3>Modifier le patient</h3>
      <form @submit.prevent="updatePatient">
        <input v-model="editingPatient.firstName" placeholder="Prénom" required />
        <input v-model="editingPatient.lastName" placeholder="Nom" required />
        <select v-model="editingPatient.gender" required>
          <option value="M">M</option>
          <option value="F">F</option>
        </select>
        <input v-model="editingPatient.dateOfBirth" type="date" required />
        <input v-model="editingPatient.address" placeholder="Adresse" required />
        <input v-model="editingPatient.phoneNumber" placeholder="Téléphone" required />
        <button type="submit">💾 Enregistrer</button>
        <button type="button" @click="editingPatient = null">❌ Annuler</button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'

const route = useRoute()
const patient = ref(null)
const notes = ref([])
const riskLevel = ref(null)
const editingPatient = ref(null)
const editingNote = ref(null)

onMounted(async () => {
  const id = route.params.id
  await fetchPatient(id)
  await fetchNotes(id)
  await fetchRisk(id)
})

const fetchPatient = async (id) => {
  try {
    const res = await axios.get(`http://localhost:8080/patient/${id}`)
    patient.value = res.data
  } catch (e) {
    console.error('Erreur récupération patient:', e)
  }
}

const fetchNotes = async (id) => {
  try {
    const res = await axios.get(`http://localhost:8080/notes/patient/${id}`)
    notes.value = res.data
  } catch (e) {
    console.error('Erreur récupération notes:', e)
  }
}

const fetchRisk = async (id) => {
  try {
    const res = await axios.get(`http://localhost:8080/assess/${id}`)
    riskLevel.value = res.data.riskLevel
  } catch (e) {
    console.error('Erreur évaluation risque:', e)
  }
}

const updatePatient = async () => {
  try {
    const id = editingPatient.value.id
    await axios.put(`http://localhost:8080/patient/${id}`, editingPatient.value)
    editingPatient.value = null
    await fetchPatient(id)
  } catch (e) {
    console.error('❌ Échec mise à jour patient:', e)
  }
}

const startEditNote = (note) => {
  editingNote.value = {
    id: note.id,
    content: note.content,
    patientId: patient.value.id // 👈 nécessaire pour l'update backend
  }
}

const cancelEditNote = () => {
  editingNote.value = null
}

const updateNote = async () => {
  try {
    const id = editingNote.value.id
    await axios.put(`http://localhost:8080/notes/${id}`, editingNote.value)
    editingNote.value = null
    await fetchNotes(patient.value.id)
  } catch (e) {
    console.error('❌ Erreur mise à jour note:', e)
  }
}

const deleteNote = async (id) => {
  console.log("📌 Tentative de suppression de la note ID:", id) // <--- ici
  if (!confirm('Supprimer cette note ?')) return
  try {
    await axios.delete(`http://localhost:8080/notes/${id}`)
    await fetchNotes(patient.value.id)
  } catch (e) {
    console.error('❌ Erreur suppression note:', e)
  }
}
</script>

<style scoped>
.content-container {
  padding-top: 200px;
}
.details-wrapper {
  background-color: white;
  padding: 2rem;
  margin: 2rem auto;
  border-radius: 10px;
  max-width: 600px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}
.risk-box {
  margin-top: 2rem;
  border-top: 1px solid #ccc;
  padding-top: 1rem;
}
.notes-box {
  margin-top: 2rem;
  border-top: 1px solid #ccc;
  padding-top: 1rem;
}
.notes-box li {
  background: #f8f8f8;
  margin: 0.5rem 0;
  padding: 0.5rem;
  border-radius: 5px;
}
.edit-form {
  background: #f9f9f9;
  padding: 1rem;
  margin-top: 2rem;
  border-radius: 8px;
  border: 1px solid #ccc;
}
</style>
