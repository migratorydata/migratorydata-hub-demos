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
                <li>Microsoft Visual Studio 2019</li>
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
git clone https://github.com/migratorydata/getting-started-cpp-client-api.git
                </code>
            </pre>

                                    Copy the following code snippet to the file <code>include/config.h</code> of your project:

                                            <pre class="language-cpp container" data-prismjs-copy="Copy">
                <code class="language-cpp">
// For API documentation go to [Developer s Guide](https://migratorydata.com/docs/client-api/cpp/)
#include &lt;string&gt;
static bool ENCRYPTION = true;
static std::string TOKEN = &quot;{{ token }}&quot;;
static std::string SERVER = &quot;{{ server }}&quot;;
static std::string SUBJECT = &quot;{{ endpoint.endpoint }}&quot;;
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
                                Use the following commands to build and run your project:
                                    <pre class="language-bash container" style="min-width:330px;" data-prismjs-copy="Copy">
                <code class="language-bash">
build.bat
start.bat
                </code>
            </pre>
                            </div>
                        </div>


</div>
</template>


