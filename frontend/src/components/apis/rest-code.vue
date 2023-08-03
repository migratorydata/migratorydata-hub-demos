<script setup>
import { onMounted } from 'vue';

defineProps(["endpoint", "token", "server"]);

onMounted(() =>  {
    Prism.highlightAll()
});
</script>


<template>
    <div>

        <div class="d-flex mb-1">
    <div class="d-flex flex-column pr-4 align-items-center">
        <i class="bi bi-2-circle fs-3"></i>
        <div class="line h-100"></div>
    </div>
    <div>
        <h5 class="text-dark mx-2 mt-2 mb-4">Prerequisites</h5>
            <ul>
                <li>curl</li>
            </ul>
    </div>
</div>

<div class="d-flex mb-1">
    <div class="d-flex flex-column pr-4 align-items-center">
        <i class="bi bi-3-circle fs-3"></i>
        <div class="line h-100"></div>
    </div>
    <div>
        <h5 class="text-dark mx-2 mt-2 mb-4">Getting started project</h5>

                                <!-- java -->
                                N/A

    </div>
</div>

                        <div class="d-flex mb-1">
                            <div class="d-flex flex-column pr-4 align-items-center">
                                <i class="bi bi-4-circle fs-3"></i>
                                <div class="line h-100 d-none"></div>
                            </div>
                            <div>
                                <h5 class="text-dark mx-2 mt-2 mb-4">Build and run</h5>
                                Copy the commands below to a terminal to send a message:


        <pre class="language-bash container" style="min-width:330px;" data-prismjs-copy="Copy">
<code class="language-bash">
TOKEN={{ token }}
curl -d &quot;some message&quot; -X POST &quot;https://{{ server }}/rest/produce?token=${TOKEN}&amp;subject={{ endpoint.endpoint }}&quot;</code>
</pre>
                            </div>
                        </div>

    </div>
</template>

