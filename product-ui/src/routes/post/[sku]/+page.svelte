<script>
    import Button from '@smui/button';
    /** @type {import('./$types').PageData} */
    export let data;
    import Textfield from '@smui/textfield';
    import HelperText from '@smui/textfield/helper-text';
    import Card, { Content } from '@smui/card';
    import { goto } from '$app/navigation';
    import { PUBLIC_PRODUCT_BASE_URL } from '$env/static/public';
    let nameValue = data.name;
    let articleIdValue = data.articleId;

    async function editProduct() {
        const res = await fetch(`${PUBLIC_PRODUCT_BASE_URL}/${data.articleId}`, {
            method: 'PATCH',
            headers: {
                'Authorization': `Basic ${btoa('client:secret')}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                name: nameValue,
                articleId: articleIdValue
            })
        }).then((res) => {
            res.json();
            goto('/');
        });
    }
</script>

<h1>Edit Product</h1>
<div class="card-display">
    <div class="card-container">
        <Card padded>
            <Textfield input$id='articleId' variant="outlined" bind:value={articleIdValue} label="articleId" disabled="true">
                <HelperText slot="articleId">articleId Helper Text</HelperText>
            </Textfield>
            <br />
            <Textfield input$id='editname' variant="outlined" bind:value={nameValue} label="Edit Name">
                <HelperText slot="Edit Name">Name Helper Text</HelperText>
            </Textfield>
            <br />
            <Button id='edit' on:click={editProduct}>Edit</Button>
        </Card>
    </div>
</div>