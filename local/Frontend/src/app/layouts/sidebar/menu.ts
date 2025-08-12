import { MenuItem } from "./menu.model";



export const MENU1 = {
    adminMenu: [
        {
            id: 1,
            label: 'MENUITEMS.MENU.TEXT',
            isTitle: true
        },
        {
            id: 2,
            label: 'dashboard',
            icon: 'bx bx-line-chart',
            link: '/analytics',
        },
        {
            id: 3,
            label: 'Les activités',
            icon: 'bxs bxs-heart',
            link: '/users/groups',
        },
        {
            id: 4,
            label: 'Les coachs',
            icon: 'bx bxs-user',
            link: '/users/coachs',
        },
        {
            id: 5,
            label: 'Les adhérents actifs',
            icon: 'bx bxs-user',
            link: '/users/user-externe',
        },
        {
            id: 6,
            label: 'Abonnements expirés',
            icon: 'bx bxs-user',
            link: '/users/abonnement_expirés',
        },


        
      


    ],


   
};



export const MENU: MenuItem[] = [ ]