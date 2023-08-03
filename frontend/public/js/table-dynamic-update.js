
function DynamicTable(tableJson) {
    this.firstTableLoad = false;
    this.tableTimes = new Map();
    this.blinkTime = 3000;
    this.blinkColor = "#F6AE2D";
    this.divTable = tableJson;   
    this.blinkInterval = null; 
}

DynamicTable.prototype.createOrUpdateTable = function(message) {
    try {
        var messagesJson = JSON.parse(message);

        // exclude elements from the table keys that in json end with '*' character
        // for (var i = 0; i < messagesJson.length; i++) {
        //     var messageJson = messagesJson[i];
        //     var jsonKeys = Object.keys(messageJson);
        //     for (var j = 0; j < jsonKeys.length; j++) {
        //         var keyValue = jsonKeys[j];
        //         if (keyValue.charAt(keyValue.length - 1) == '*') {
        //             delete messageJson[jsonKeys[j]];
        //         }
        //     }
        // }
        var divTableLength = this.divTable.innerHTML.length;
        if (divTableLength == 0) {
            var jsonHtmlTable = ConvertJsonToTable(messagesJson, null, null, "Info");
            // 99 is size of a empty table
            if (jsonHtmlTable.length > 99) {
                this.divTable.innerHTML = jsonHtmlTable;
                // //document.getElementById("table-tab-change").children[0].style.display = "";
                // if (this.firstTableLoad == false) {
                //     //$('[href="#table-view"]').tab('show');
                this.blinkInterval = setInterval(() => this.blink(), 1000);
                // }
            } else {
                //document.getElementById("table-tab-change").children[0].style.display = "none";
            }
        } else {
            this.updateTable(messagesJson);
        }
        this.firstTableLoad = true;
    } catch (e) {
        //document.getElementById("table-tab-change").children[0].style.display = "none";
        console.log(e);
    }

}

DynamicTable.prototype.updateTable = function(messagesJson) {
    var link = '<a href="{0}">Info</a>';

    var myTable = this.divTable.querySelector("#myTable");
    // console.log(myTable);
    var tableHeads = myTable.querySelectorAll("thead")[0].querySelectorAll("tr")[0].querySelectorAll("th");
    var ths = [];

    // get all table headers
    for (var i = 0; i < tableHeads.length; i++) {
        ths.push(tableHeads[i].innerText);
    }

    // iterate through all jsons in the list
    for (var i = 0; i < messagesJson.length; i++) {
        var messageJson = messagesJson[i];

        myTable = this.divTable.querySelector("#myTable");
        // all body tr elements
        var bodyTrs = myTable.querySelectorAll("tbody")[0].querySelectorAll("tr");

        // get primary key element from json(primary key element is considered the first key of the first json in the message with which the table was creaeted)
        var keyField = messageJson[ths[0]];

        // check if current json has the primary key in his keys
        if (keyField != null) {

            var lineExists = false;
            // iterate throug body trs
            for (var j = 0; j < bodyTrs.length; j++) {

                var tds = bodyTrs[j].querySelectorAll("td");

                // check if current tr tag  has the same primary key as current json 
                if (tds[0].innerText == keyField) {
                    lineExists = true;
                    // iterate through all line values
                    for (var k = 0; k < ths.length; k++) {
                        // get value in the json as per the k-th key in the table header
                        var jsonValue = messageJson[ths[k]];
                        if (jsonValue != null && jsonValue != tds[k].innerText) {
                            if (ths[k] == "link*") {
                                tds[k].innerHtml = link.format(jsonValue);
                            } else {
                                tds[k].innerText = jsonValue;
                                var element = tds[k];
                                element.style.backgroundColor = this.blinkColor;
                                this.tableTimes.set(keyField, new Date());    
                            }
                        }
                    }
                    break;
                }
            }

            // ADD NEW ROW
            // if the current primary key was not found in the table rows that mmeans that the row need to be added to the table
            if (lineExists == false) {
                var newRow = "<tr>";
                for (var k = 0; k < ths.length; k++) {
                    newRow += "<td>";
                    newRow += messageJson[ths[k]];
                    newRow += "</td>";
                }
                newRow += "</tr>";
                this.divTable.querySelector("#myTableBody").innerHTML += newRow;

                var newBodyTrs = myTable.querySelectorAll("tbody")[0].querySelectorAll("tr");
                var element = newBodyTrs[newBodyTrs.length - 1];
                var tds = element.querySelectorAll("td");
                for (var t = 0; t < tds.length; t++) {
                    tds[t].style.backgroundColor = this.blinkColor;
                }
                this.tableTimes.set(messageJson[ths[0]], new Date());
            }
        }
    }

    myTable = this.divTable.querySelector("#myTable");
    // REMOVE ROWS THAT DO NOT APPEAR
    bodyTrs = myTable.querySelectorAll("tbody")[0].querySelectorAll("tr");
    var deletedRows = 0;
    for (var j = 0; j < bodyTrs.length; j++) {
        myTable = this.divTable.querySelector("#myTable");
        var tds = bodyTrs[j].querySelectorAll("td");
        var found = false;
        for (var i = 0; i < messagesJson.length; i++) {
            var messageJson = messagesJson[i];
            if (tds[0].innerText == messageJson[ths[0]]) {
                found = true;
                break;
            }
        }
        if (found == false) {
            myTable.getElementsByTagName("tr")[j + 1 - deletedRows].remove();
            deletedRows++;
        }
    }
}

DynamicTable.prototype.blink = function() {
    var now = new Date();
    var myTable = this.divTable.querySelector("#myTable");
    var bodyTrs = myTable.querySelectorAll("tbody")[0].querySelectorAll("tr");
    for (var j = 0; j < bodyTrs.length; j++) {
        var tds = bodyTrs[j].querySelectorAll("td");
        if (this.tableTimes.has(tds[0].innerText) == true) {
            if (now - this.tableTimes.get(tds[0].innerText) > this.blinkTime) {
                for (var k = 0; k < tds.length; k++) {
                    tds[k].style.backgroundColor = "";
                }
                this.tableTimes.delete(tds[0].innerText);
            }
        }
    }
}

DynamicTable.prototype.remove = function() {
    // this.divTable.childNodes[0].remove();
    // console.log(this.divTable);
    // this.divTable.parentNode.removeChild(this.divTable);
    var myTable = this.divTable.querySelector("#myTable");
    if (myTable) 
        myTable.parentNode.removeChild(myTable)
    
    if (this.blinkInterval) {
        clearInterval(this.blinkInterval);
        this.blinkInterval = null;
    }
}