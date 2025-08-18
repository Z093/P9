<template>
  <div class="content-container">
    <div class="details-wrapper" v-if="patient">
      <h2>🧾 Détails du patient</h2>
      <ul>
        <li><strong>ID :</strong> {{ patient.id }}</li>
        <li><strong>Prénom :</strong> {{ patient.firstName }}</li>
        <li><strong>Nom :</strong> {{ patient.lastName }}</li>
        <li><strong>Date de naissance :</strong> {{ formatDate(patient.dateOfBirth) }}</li>
        <li><strong>Sexe :</strong> {{ patient.gender === 'M' ? 'Homme' : 'Femme' }}</li>
        <li><strong>Téléphone :</strong> {{ patient.phoneNumber }}</li>
        <li><strong>Adresse :</strong> {{ patient.address }}</li>
      </ul>

      <button class="edit-btn" @click="goToEdit">
        ✏️ Modifier le patient
      </button>

      <div class="risk-box" v-if="riskLevel">
        <h3>🧪 Évaluation du risque :</h3>
        <p><strong>Résultat :</strong> {{ riskLevel }}</p>
      </div>

      <div class="notes-box">
        <h3>📝 Notes médicales :</h3>
        <ul v-if="notes.length">
          <li
              v-for="note in notes"
              :key="note.id"
              :class="{ editing: editingNote && editingNote.id === note.id }"
          >
            <!-- Mode édition : zone agrandie -->
            <div v-if="editingNote && editingNote.id === note.id" class="note-editor">
              <textarea
                  v-model="editingNote.content"
                  rows="12"
                  placeholder="Éditez la note..."
              ></textarea>

              <div class="note-actions">
                <button @click="updateNote()">💾 Sauvegarder</button>
                <button @click="cancelEditNote">❌ Annuler</button>
              </div>
            </div>

            <!-- Mode lecture -->
            <div v-else>
              <p>{{ note.content }}</p>
              <div class="note-actions">
                <button @click="startEditNote(note)">✏️ Modifier</button>
                <button @click="deleteNote(note.id)">🗑️ Supprimer</button>
              </div>
            </div>
          </li>
        </ul>
        <p v-else>Aucune note pour ce patient.</p>
      </div>
    </div>

    <p v-else>Chargement…</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'

const route = useRoute()
const router = useRouter()

const patient = ref(null)
const notes = ref([])
const riskLevel = ref(null)
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

const startEditNote = (note) => {
  editingNote.value = {
    id: note.id,
    content: note.content,
    patientId: patient.value.id // requis pour backend
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
  console.log("📌 Tentative de suppression de la note ID:", id)
  if (!confirm('Supprimer cette note ?')) return
  try {
    await axios.delete(`http://localhost:8080/notes/${id}`)
    await fetchNotes(patient.value.id)
  } catch (e) {
    console.error('❌ Erreur suppression note:', e)
  }
}

const goToEdit = () => {
  router.push(`/patient/${route.params.id}/edit`)
}

const formatDate = (value) => {
  if (!value) return ''
  const d = new Date(value)
  if (isNaN(d)) return value
  const dd = String(d.getDate()).padStart(2, '0')
  const mm = String(d.getMonth() + 1).padStart(2, '0')
  const yyyy = d.getFullYear()
  return `${dd}/${mm}/${yyyy}`
}
</script>

<style scoped>
.content-container {
  padding-top: 100px;
  min-height: 500px;
}

.details-wrapper {
  background-color: white;
  padding: 2rem;
  margin: 2rem auto;
  border-radius: 10px;
  max-width: 600px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.edit-btn {
  margin-top: 1rem;
  border: none;
  background: #4f46e5;
  color: #fff;
  padding: .6rem 1rem;
  border-radius: 8px;
  cursor: pointer;
}
.edit-btn:hover { opacity: .9; }

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




.notes-box li.editing {
  background: #ffffff;
  padding: 1rem;
  border: 1px solid #e5e7eb;
  box-shadow: 0 6px 18px rgba(0,0,0,0.08);
}


.note-editor {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}


.note-editor textarea {
  width: 100%;
  min-height: 220px;
  padding: 0.75rem;
  border: 1px solid #bbb;
  border-radius: 8px;
  box-sizing: border-box;
  line-height: 1.5;
  font-size: 1rem;
  resize: vertical;
}


.note-actions {
  display: flex;
  gap: .5rem;
  margin-top: .5rem;
  justify-content: flex-end;
}
</style>
