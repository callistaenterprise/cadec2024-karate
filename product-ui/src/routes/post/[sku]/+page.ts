import { PUBLIC_PRODUCT_BASE_URL } from '$env/static/public';
/** @type {import('./$types').PageLoad} */
export async function load({ params }) {
    const { articleId } = params
    var headers = new Headers({
        'Authorization': `Basic ${btoa('client:secret')}`
      });
const data = await fetch(`${PUBLIC_PRODUCT_BASE_URL}/${articleId}`, {headers: headers}).then(res => res.json());
    return data
}