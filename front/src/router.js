import { createRouter, createWebHistory } from 'vue-router'
import AddPatient from './views/AddPatient.vue'
import AddNote from './views/AddNote.vue'
import PatientList from './views/PatientList.vue'
import PatientDetails from './views/PatientDetails.vue'

const routes = [
    { path: '/', redirect: '/patients' },
    { path: '/patients', component: PatientList },
    { path: '/add', component: AddPatient },
    { path: '/note', component: AddNote },
    { path: '/patient/:id', component: PatientDetails },
]

export default createRouter({
    history: createWebHistory(),
    routes,
})
