<script lang="ts">
    import Textfield from '@smui/textfield';
    import HelperText from '@smui/textfield/helper-text';
    import Button from '@smui/button';
    /** @type {import('./$types').PageData} */
    import Card, { Content } from '@smui/card';
    let username = "";
    let password = "";
</script>

<h1>Login</h1>
<div class="card-display">
    <div class="card-container">
        <Card padded>
            <form method="POST">
                <Textfield input$id='username' input$name='username' variant="outlined" bind:value={username} label="Username">
                    <HelperText slot="Username">Username Helper Text</HelperText>
                </Textfield>
                <br />
                <Textfield input$id='password' input$name='password' variant="outlined" bind:value={password} label="Password">
                    <HelperText slot="Password">Password Helper Text</HelperText>
                </Textfield>
                <br />
                <Button id="login" type="submit">LogIn</Button>
            </form>
        </Card>
    </div>
</div>