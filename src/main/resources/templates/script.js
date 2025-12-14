// import Paho from "paho-mqtt";

const url = `ws://192.168.1.71:15675/ws`;
const options = {
    keepalive: 60,
    clientId: 'mqttjs_' + Math.random().toString(16).substr(2, 8),
    username: 'root',
    password: 'root123',
    clean: true,
    reconnectPeriod: 1000,
    connectTimeout: 30 * 1000,
};

const topic = "my/routing/key";
const client = mqtt.connect(url, options);
client.on('connect', () => {
    console.log('Connected to MQTT broker');
    inputBox.disabled = false;

    client.subscribe(topic,  { qos: 1 },  err => {
        if (!err) {
            console.log("Subscribed to topic : " + topic);
        }
    })
});


const box = document.querySelector(".box");
const responseElement = box.querySelector(".response");
const inputBox = document.getElementById("text-box");

box.addEventListener("submit", (e) => {
    e.preventDefault();
    const message = e.target.querySelector("input").value;
    client.publish(topic, message, { qos: 1 }, err => {
        if (!err) {
            console.log("Message sent successfully. PUBACK received.");
        }
    });
    responseElement.innerText = `You sent: ${message}`;
});







