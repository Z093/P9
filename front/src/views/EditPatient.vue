<template>
  <div class="content-wrapper">
    <h2 style="margin-bottom: 1em;">✏️ Modifier le patient</h2>

    <form v-if="form" @submit.prevent="updatePatient" style="display: flex; flex-direction: column; gap: 1em;">
      <div>
        <label>Prénom</label><br />
        <input
            v-model="form.firstName"
            placeholder="Jean"
            required
            style="width: 100%; padding: 0.5em;"
        />
      </div>

      <div>
        <label>Nom</label><br />
        <input
            v-model="form.lastName"
            placeholder="Dupont"
            required
            style="width: 100%; padding: 0.5em;"
        />
      </div>

      <div>
        <label>Genre</label><br />
        <select
            v-model="form.gender"
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
            v-model="form.dateOfBirth"
            type="date"
            required
            style="width: 100%; padding: 0.5em;"
        />
      </div>

      <div>
        <label>Adresse</label><br />
        <input
            v-model="form.address"
            placeholder="123 rue Exemple"
            required
            style="width: 100%; padding: 0.5em;"
        />
      </div>

      <div>
        <label>Téléphone</label><br />
        <input
            v-model="form.phoneNumber"
            placeholder="0123456789"
            required
            style="width: 100%; padding: 0.5em;"
        />
      </div>

      <div style="display: flex; gap: .6em;">
        <button
            type="submit"
            style="padding: 0.6em; background-color: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer;"
        >
          💾 Enregistrer
        </button>

        <button
            type="button"
            @click="goBack"
            style="padding: 0.6em; background-color: #6c757d; color: white; border: none; border-radius: 4px; cursor: pointer;"
        >
          ❌ Annuler
        </button>
      </div>
    </form>

    <p v-else>Chargement…</p>

    <p v-if="message" :style="{ color: messageColor, fontWeight: 'bold', marginTop: '1em' }">
      {{ message }}
    </p>
  </div>
</template>

<script setup>
import {ref, onMounted} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import axios from 'axios'

const route = useRoute()
const router = useRouter()

const form = ref(null)
const message = ref('')
const messageColor = ref('green')

onMounted(async () => {
  const id = route.params.id
  try {
    const {data} = await axios.get(`http://localhost:8080/patient/${id}`)
    // clone + normalisation de la date pour l'input date
    form.value = {...data}
    if (form.value?.dateOfBirth) {
      const d = new Date(form.value.dateOfBirth)
      if (!isNaN(d)) form.value.dateOfBirth = d.toISOString().slice(0, 10)
    }
    // Sécurité : si pas de genre, afficher l’option disabled
    if (!form.value.gender) form.value.gender = ''
  } catch (e) {
    console.error('Erreur chargement patient:', e)
    message.value = '❌ Impossible de charger le patient.'
    messageColor.value = 'red'
  }
})

const updatePatient = async () => {
  try {
    const id = route.params.id
    await axios.put(`http://localhost:8080/patient/${id}`, form.value)
    message.value = '✅ Patient mis à jour avec succès.'
    messageColor.value = 'green'
    // Retour à la fiche après un court délai pour laisser voir le message (facultatif)
    setTimeout(() => router.push(`/patient/${id}`), 400)
  } catch (e) {
    console.error('❌ Échec mise à jour patient:', e)
    message.value = '❌ Échec de la mise à jour du patient.'
    messageColor.value = 'red'
  }
}

const goBack = () => {
  router.push(`/patient/${route.params.id}`)
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

  /* pour que ça s’aligne visuellement comme ta page “Ajouter” si tu l’affiches au centre */
  margin: 0 auto;
}
</style>
