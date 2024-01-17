<h1>Product Admin</h1>
<script lang="ts">
    import { onMount } from 'svelte';
    import Table from "../components/Table.svelte"
    import Button from '@smui/button';
    import Dialog from '../components/Dialog.svelte';
    import { PUBLIC_PRODUCT_BASE_URL } from '$env/static/public';

    type Product = {
      productId: number;
      name: string;
      articleId: string;
      inventory: number;
     };
  
    let items: Product[] = [];
    let loaded = false;
    let open = false;

    onMount(() => loadThings(false))

    function loadThings(wait: boolean) {
            if (typeof fetch !== 'undefined') {
                loaded = false;
                var headers = new Headers({
                'Authorization': `Basic ${btoa('client:secret')}`
                });
                fetch(PUBLIC_PRODUCT_BASE_URL, {headers: headers})
                    .then((response) => response.json())
                    .then((json) =>
                        setTimeout(
                            () => {
                                items = json;
                                loaded = true;
                            },
                            // Simulate a long load time.
                            wait ? 2000 : 0
                        )
                    );
            }
    }

    function beforeClose() {
        loadThings(false);
        return true;
    }
</script>

<div style="display:flex; justify-content:space-between">
    <Button on:click={() => (open = true)}>Add New</Button>
    <Button id='logout' href={`/logout`}>Log Out</Button>
</div>
<Table items={items} loaded={loaded}/>

<Dialog open={open} beforeClose={beforeClose}/>
