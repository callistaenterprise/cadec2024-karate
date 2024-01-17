import { fail, redirect } from '@sveltejs/kit';
import { VITE_ADMIN_USERNAME, VITE_ADMIN_PASSWORD } from "$env/static/private";

export const actions = {
	default: async ({ request, cookies }) => {
		const form = await request.formData();
		const username = form.get('username');
		const password = form.get('password');

		if (username === '' || password === '') {
			throw redirect(307, '/login');
		}
        if(username !== VITE_ADMIN_USERNAME|| password !== VITE_ADMIN_PASSWORD) {
            return fail(400, { username, incorrect: true });
        }
 
		cookies.set('session_id', username, {
			path: '/',
			httpOnly: true,
			sameSite: 'strict',
			secure: true,
			maxAge: 60 * 60 * 24 * 7 // one week
		});
        throw redirect(303, "/")
	}
};