function timeDifference(current, previous) {

    var msPerMinute = 60 * 1000;
    var msPerHour = msPerMinute * 60;
    var msPerDay = msPerHour * 24;
    var msPerMonth = msPerDay * 30;
    var msPerYear = msPerDay * 365;

    var elapsed = current - previous;

    if (elapsed < msPerMinute) {
        var elapsedSeconds = Math.round(elapsed / 1000);
        if (elapsedSeconds < 10) {
            return 'now';
        }
        return Math.round(elapsed / 1000) + ' seconds ago';
    } else if (elapsed < msPerHour) {
        return Math.round(elapsed / msPerMinute) + ' minutes ago';
    } else if (elapsed < msPerDay) {
        return Math.round(elapsed / msPerHour) + ' hours ago';
    } else if (elapsed < msPerMonth) {
        return 'approximately ' + Math.round(elapsed / msPerDay) + ' days ago';
    } else if (elapsed < msPerYear) {
        return 'approximately ' + Math.round(elapsed / msPerMonth) + ' months ago';
    } else {
        return 'approximately ' + Math.round(elapsed / msPerYear) + ' years ago';
    }
}

function getLastSegment(endpoint) {
    return endpoint.substr(endpoint.indexOf("/", endpoint.indexOf("/", 1) + 1) + 1);
}

function updateDataTabWhenSelectEndPoint (newEndpoint, allData, update) {
    // endpoint with symbol
    if (!newEndpoint) return;

    var endpoint = newEndpoint.endpoint;
    if (newEndpoint.endpointExample) {
        endpoint = newEndpoint.endpointExample;
    }
    var subjects = Object.keys(allData);
    var data = {};
    for (const subject of subjects) {
        if (compareEndpointWithSubject(endpoint, subject)) {
            data = allData[subject];
            break;
        }
    }
    update(data);
}

function updateDataTabWhenDataReceived(endpoint, message, update) {
    if (!endpoint) return;

    var e = endpoint.endpoint;
    if (endpoint.endpointExample) {
        e = endpoint.endpointExample;
    }
    if (compareEndpointWithSubject(e, message.subject)) {
        update(JSON.parse(message.content));
    }
}

function compareEndpointWithSubject(endpoint, subject) {
    while (true) {
        var endpointIndex = endpoint.indexOf("/", 1);
        var subjectIndex = subject.indexOf("/", 1);
        if (endpointIndex == -1 || subjectIndex == -1) {
            if (compareSegments(endpoint, subject)) {
                return true;
            }
            return false;
        }

        var endpointSegment = endpoint.substring(1, endpointIndex);
        var subjectSegment = subject.substring(1, subjectIndex);

        if (!compareSegments(endpointSegment, subjectSegment)) {
            return false;
        }

        endpoint = endpoint.substring(endpointIndex + 1);
        subject = subject.substring(endpointIndex + 1);
        if (endpoint == null || subject == null) {
            return true;
        }
    }
}

function compareSegments(endpointSegment, subjectSegment) {
    if (endpointSegment == subjectSegment) {
        return true;
    }
    return false;
}
