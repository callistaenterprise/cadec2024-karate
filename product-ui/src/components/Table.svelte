<DataTable table$aria-label="Product list" style="width: 100%;">
    <Head>
        <Row>
            <Cell numeric>Product ID</Cell>
            <Cell>Article ID</Cell>
            <Cell>Name</Cell>
            <Cell numeric>Inventory</Cell>
            <Cell>Actions</Cell>
        </Row>
    </Head>
    <Body>
        {#each items as item (item.productId)}
            <Row>
                <Cell numeric>{item.productId}</Cell>
                <Cell>{item.articleId}</Cell>
                <Cell>{item.name}</Cell>
                <Cell numeric>{item.inventory}</Cell>
                <Cell>
                    <Button id={`edit_${item.articleId}`} href={`/post/${item.articleId}`}>Edit</Button>
                    <Button on:click={() => deleteProduct(item.articleId)}>Delete</Button>
                </Cell>
            </Row>
        {/each}
    </Body>

    <LinearProgress
        indeterminate
        bind:closed={loaded}
        aria-label="Data is being loaded..."
        slot="progress"
    />
</DataTable>

<script lang="ts">
    import DataTable, { Head, Body, Row, Cell } from '@smui/data-table';
    import LinearProgress from '@smui/linear-progress';
    import Button from '@smui/button';
    import { PUBLIC_PRODUCT_BASE_URL } from '$env/static/public';

    export let items: any[] = []
    export let loaded = false

    async function deleteProduct(articleId: string) {
    const res = await fetch(`${PUBLIC_PRODUCT_BASE_URL}/${articleId}`, {
        method: 'DELETE',
        headers: {
                'Authorization': `Basic ${btoa('client:secret')}`
            },
    }).then((res) => {
        res.json();
        location.reload();
    });
}</script>