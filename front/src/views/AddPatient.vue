<template>
  <div class="content-wrapper">
    <h2 style="margin-bottom: 1em;">👤 Ajouter un patient</h2>
    <form @submit.prevent="addPatient" style="display: flex; flex-direction: column; gap: 1em;">
      <div>
        <label>Prénom</label><br />
        <input
            v-model="newPatient.firstName"
            placeholder="Jean"
            required
            style="width: 100%; padding: 0.5em;"
        />
      </div>

      <div>
        <label>Nom</label><br />
        <input
            v-model="newPatient.lastName"
            placeholder="Dupont"
            required
            style="width: 100%; padding: 0.5em;"
        />
      </div>

      <div>
        <label>Genre</label><br />
        <select
            v-model="newPatient.gender"
            required
            style="width: 100%; padding: 0.5em;"
        >
          <option disabled value="">Sélectionner le genre</option>
          <option value="M">Homme</option>
          <option value="F">Femme</option>
        </select>
      </div>

      <div>
        <label>Date de naissance</label><br />
        <input
            v-model="newPatient.dateOfBirth"
            type="date"
            required
            style="width: 100%; padding: 0.5em;"
        />
      </div>

      <div>
        <label>Adresse</label><br />
        <input
            v-model="newPatient.address"
            placeholder="123 rue Exemple"
            required
            style="width: 100%; padding: 0.5em;"
        />
      </div>

      <div>
        <label>Téléphone</label><br />
        <input
            v-model="newPatient.phoneNumber"
            placeholder="0123456789"
            required
            style="width: 100%; padding: 0.5em;"
        />
      </div>

      <button
          type="submit"
          style="padding: 0.6em; background-color: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer;"
      >
        ➕ Ajouter
      </button>
    </form>

    <p v-if="patientMessage" style="color: green; font-weight: bold; margin-top: 1em;">
      {{ patientMessage }}
    </p>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'

const newPatient = ref({
  firstName: '',
  lastName: '',
  gender: '',
  dateOfBirth: '',
  address: '',
  phoneNumber: '',
})

const patientMessage = ref('')

const addPatient = async () => {
  try {
    await axios.post('http://localhost:8080/patient', newPatient.value)
    patientMessage.value = '✅ Patient ajouté avec succès.'
    Object.keys(newPatient.value).forEach(k => (newPatient.value[k] = ''))
  } catch (e) {
    console.error('❌ Erreur ajout patient:', e)
    patientMessage.value = '❌ Échec de l\'ajout du patient.'
  }
}
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
