// import { Terminal } from './xterm.js';
// import { AttachAddon } from './xterm-addon-attach.js';

(function () {
    const term = new Terminal({
        screenKeys: true,
        useStyle: true,
        cursorBlink: true,
        fullscreenWin: true,
        maximizeWin: true,
        screenReaderMode: true,
        cols: 128,
    });
    term.open(document.getElementById('terminal'));
    const protocol = (location.protocol === "https:") ? "wss://" : "ws://";
    const url = protocol + location.host + "/terminal";
    const socket = new WebSocket(url);
    const attachAddon = new AttachAddon.AttachAddon(socket);
    // const fitAddon = new FitAddon.FitAddon();
    // term.loadAddon(fitAddon);
    socket.onclose = function (event) {
        console.log(event);
        term.write('\r\n\nconnection has been terminated\n')
    };
    socket.onopen = function () {
        console.log('socket ist offen')
        term.loadAddon(attachAddon);
        term._initialized = true;
        term.focus();
        // setTimeout(function () {fitAddon.fit()});
        // window.onresize = function () {
        //     fitAddon.fit();
        // }
        term.write('bash-5.1$ ')
    };
})();
