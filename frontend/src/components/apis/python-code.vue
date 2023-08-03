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
                <li>Python 3+</li>
                <li>git</li>
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

                                    Download the getting started project:

                                    <pre class="language-bash container" data-prismjs-copy="Copy">
                <code class="language-bash">
git clone https://github.com/migratorydata/getting-started-python-client-api.git
                </code>
            </pre>

                                    Copy the code snippet below to the file <code>sample/config.py</code> of your project:


                                            <pre class="language-python" data-prismjs-copy="Copy">
                <code class="language-python container">
# For API documentation go to [Developer&rsquo;s Guide](https://migratorydata.com/docs/client-api/python/)
class Config:
    server = [&quot;{{ server }}&quot;]
    encryption = True
    subject = &quot;{{ endpoint.endpoint }}&quot;
    token = &quot;{{ token }}&quot;
                </code>
            </pre>

    </div>
</div>

                        <div class="d-flex mb-1">
                            <div class="d-flex flex-column pr-4 align-items-center">
                                <i class="bi bi-4-circle fs-3"></i>
                                <div class="line h-100 d-none"></div>
                            </div>
                            <div>
                                <h5 class="text-dark mx-2 mt-2 mb-4">Build and run</h5>
                                Use the following command to run your project:
                                    <pre class="language-bash container" style="min-width:330px;" data-prismjs-copy="Copy">
                <code class="language-bash">
python sample
                </code>
            </pre>
                            </div>
                        </div>


</div>
</template>

