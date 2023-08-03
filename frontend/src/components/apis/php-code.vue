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
                <li>php</li>
                <li>composer</li>
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

        Create a folder for your getting started project. Change to your project folder and add the
                                    MigratoryData library as follows:

                                    <pre class="language-bash container" data-prismjs-copy="Copy">
                <code class="language-bash">
composer require migratorydata/migratorydata-client-php:6.*
                </code>
            </pre>

                                    Copy the below code snippet to a file <code>sample-client.php</code> in your project folder:


                                            <pre class="language-php" data-prismjs-copy="Copy">
                <code class="language-php container ">
&lt;?php
// For API documentation go to [Developer&rsquo;s Guide](https://migratorydata.com/docs/client-api/php/)
require __DIR__ . '/vendor/autoload.php';
use MigratoryData\Client\MigratoryDataClient;
use MigratoryData\Client\MigratoryDataException;
use MigratoryData\Client\MigratoryDataMessage;
// Create a MigratoryData client
$client = new MigratoryDataClient();
try
{
    // Attach the entitlement token to the client.
    $client-&gt;setEntitlementToken(&quot;{{ token }}&quot;);
    // Connect to a MigratoryData server
    $client-&gt;setServers(array(&quot;https://{{ server }}&quot;));
    $client-&gt;connect();
}
catch (MigratoryDataException $e)
{
    // Exceptions with one of the error codes:
    //		E_INVALID_URL_LIST
    //		E_CLUSTER_MEMBERS_CONNECTION_FAILED
    //		E_ENTITLEMENT_TOKEN
    //		E_RUNNING
    // See the documenation of MigratoryDataException for more details
    echo(&quot;Exception: &quot; . $e-&gt;getDetail() . &quot;\n&quot;);
    exit(1);
}
while (true)
{
    // Publish a message
    try
    {
        $message = new MigratoryDataMessage(&quot;{{ endpoint.endpoint }}&quot;, time());
        $response = $client-&gt;publish($message);
        echo(&quot;Got response: &quot; . $response . &quot;\\n&quot;);
    }
    catch (MigratoryDataException $e)
    {
        // Exception with one of the error codes:
        //		E_NOT_CONNECTED
        //		E_MSG_NULL
        //		E_MSG_INVALID
        //		E_INVALID_SUBJECT
        //		E_INVALID_PROTOCOL
        // See the documentation of MigratoryDataException for more details
        echo(&quot;Exception: &quot; . $e-&gt;getDetail() . &quot;\\n&quot;);
    }

    sleep(3);
}
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
php sample-client.php
                </code>
            </pre>
                            </div>
                        </div>



</div>
</template>

