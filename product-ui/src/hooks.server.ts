import type { Handle } from "@sveltejs/kit";
import { redirect } from '@sveltejs/kit';

export const handle: Handle = async ({ event, resolve }) => {

    const url = new URL(event.request.url);
    if (url.pathname.startsWith("/login")) {
        return resolve(event);
    } else if (url.pathname.startsWith("/logout")) {
        await event.cookies.delete('session_id', { path: '/' });
        throw redirect(303, '/login');
    }

    const sessionId = event.cookies.get('session_id');
    if (!sessionId ) {
        throw redirect(303, '/login');
    }

	return resolve(event);
};