import { createRouter, createWebHistory } from 'vue-router'
import AddPatient from './views/AddPatient.vue'
import AddNote from './views/AddNote.vue'
import PatientList from './views/PatientList.vue'
import PatientDetails from './views/PatientDetails.vue'
import EditPatient from './views/EditPatient.vue' // ⬅️ nouveau

const routes = [
    { path: '/', redirect: '/patients' },
    { path: '/patients', component: PatientList },
    { path: '/add', component: AddPatient },
    { path: '/note', component: AddNote },
    { path: '/patient/:id', component: PatientDetails },
    { path: '/patient/:id/edit', component: EditPatient, name: 'EditPatient' }, // ⬅️ nouveau
]

export default createRouter({
    history: createWebHistory(),
    routes,
})
