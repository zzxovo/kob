<template>
    <div class="container content-field" v-if="!$store.state.user.pulling_info">
        <div class="card">
            <div class="card-body">
                <slot></slot>
            </div>
        </div>
    </div>
</template>
<script>
</script>

<style>
.content-field{
    margin-top: 20px;
}
</style>