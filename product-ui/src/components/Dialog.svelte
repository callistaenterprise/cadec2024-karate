<script lang="ts">
    // @ts-nocheck
    import Dialog, { Title, Content, Actions } from '@smui/dialog';
    import Textfield from '@smui/textfield';
    import HelperText from '@smui/textfield/helper-text';
    import Card from '@smui/card';
    import Button from '@smui/button';
    import { PUBLIC_PRODUCT_BASE_URL } from '$env/static/public';

    let name = '';
    let articleId = '';
    
    export let open = false;
    export let beforeClose = () => true;

    async function createProduct() {
        const res = await fetch(`${PUBLIC_PRODUCT_BASE_URL}`, {
            method: 'POST',
            headers: {
                'Authorization': `Basic ${btoa('client:secret')}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                name,
                articleId
            })
        }).then((res) => {
                res.json();
                location.reload();
                open = false;
        });
    }
</script>
<Dialog bind:open selection aria-labelledby="list-title" aria-describedby="list-content" beforeClose=beforeClose>
    <Title id="list-title">Create New Product</Title>
    <Content id="mandatory-content">
        <Card padded>
            <Textfield input$id='addArticleId' variant="outlined" bind:value={articleId} label="articleId">
                <HelperText slot="articleId">ArticleId Helper Text</HelperText>
            </Textfield>
            <br />
            <Textfield input$id='addName' variant="outlined" bind:value={name} label="Name">
                <HelperText slot="Name">Name Helper Text</HelperText>
            </Textfield>
            <br />
            <Button id="addProduct" on:click={createProduct}>Create</Button>
        </Card>
    </Content>
    <Actions>
        <Button action="accept">Close</Button>
    </Actions>
</Dialog>
