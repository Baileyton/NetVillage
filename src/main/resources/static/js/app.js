function getNickFromCookie() {
    var name = "nick=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var cookieArray = decodedCookie.split(';');
    for(var i = 0; i < cookieArray.length; i++) {
        var cookie = cookieArray[i].trim();
        if (cookie.indexOf(name) == 0) {
            return cookie.substring(name.length, cookie.length);
        }
    }
    return null;
}

// WebSocket 연결 설정
var stompClient = null;
function connect() {
    var socket = new SockJS('/chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/public', function(response) {
            showMessage(JSON.parse(response.body));
        });
    });
}

function showMessage(message) {
    var messageArea = document.getElementById('messageArea');
    if (!messageArea) {
        // messageArea가 없을 경우 생성
        messageArea = document.createElement('div');
        messageArea.id = 'messageArea';
        document.body.appendChild(messageArea);
    }

    var p = document.createElement('p');
    p.textContent = message.sender + ': ' + message.content;
    messageArea.appendChild(p);
}

// 메시지 전송
document.getElementById('sendButton').addEventListener('click', function() {
    var messageInput = document.getElementById('messageInput');
    var message = messageInput.value.trim();
    var sender = getNickFromCookie() || 'user'; // 쿠키에서 가져오거나 기본값으로 'user' 사용
    if (message) {
        stompClient.send('/app/chat.sendMessage', {}, JSON.stringify({ 'content': message, 'sender': sender }));
        messageInput.value = '';
    }
});

connect();