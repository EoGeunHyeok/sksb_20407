<script>
	import {onMount} from 'svelte';

	let member = {};
	let isLogin = false;

	onMount(() =>{
		//로그인한 회원정보 불러오기
		fetch('http://localhost:8090/api/v1/members/me', {
			credentials: "include"
		})
		.then(Response => Response.json())
		.then(data => {
			//성공시 데이터를 member에 담기
			console.log(data.data?.item)
			member = data.data?.item;
			isLogin = true;
		})
		.catch(error => {
			//실패시 처리
			console.error(error)
		})
	});

</script>

<header>
	<ul>
		{#if isLogin}
		<li><a href="/logout">로그아웃</a></li>
		{:else}
		<li><a href="/login">로그인</a></li>
		{/if}
	</ul>
</header>

<div>
	<h2>id : {member.username}</h2>
</div>

<slot />

