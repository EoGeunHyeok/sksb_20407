<script>
    function submitLoginForm(){
        const form = this;

        form.username.value = form.username.value.trim();

        if (form.username.value.length == 0) {
            alert('Username is required')
            form.username.focus();
            return;
        }
        form.password.value = form.password.value.trim();

    if (form.password.value.length == 0) {
    alert('password is required')
    form.password.focus();
    return;
        }
        fetch('http://localhost:8090/api/v1/members/login',{
            method: "POST",
            credentials: 'include',
            headers: {
                'Content-Type' : 'application/json' 
            },
            body: JSON.stringify({
                username: form.username.value,
                password: form.password.value
            })
        })
        .then((response) => response.json())
        .then((data)=>{
            console.log(data);
        })
        .catch((error) => {
            console.log(error);
        });
    }
</script>

<form on:submit|preventDefault={submitLoginForm}>
    <input type="text", name="username" placeholder="Username" >
    <input type="text", name="password" placeholder="Password" >
    <button type="submit">Login</button>
</form>