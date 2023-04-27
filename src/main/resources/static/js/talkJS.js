await Talk.ready;
const appId = await fetch("/keys").then(res => res.json()).then(res => res.talkJsAppId);
const loggedInUser = await fetch("/loggedInUser").then(res => res.json());
const friends = await fetch("/loggedInUserFriends").then(res => res.json());
const me = new Talk.User({
	id: loggedInUser.id,
	name: loggedInUser.username,
	email: loggedInUser.email,
	photoUrl: loggedInUser.photo,
});
const session = new Talk.Session({
	appId: appId,
	me: me,
});
const others = [];
for (const friend of friends) {
	others.push(
		new Talk.User({
			id: friend.id,
			name: friend.username,
			email: friend.email,
			photoUrl: friend.photo,
		})
	);
}
const conversations = [];
for (const other of others) {
	let conversation = session.getOrCreateConversation(
		Talk.oneOnOneId(me, other)
	);
	conversation.setParticipant(me)
	conversation.setParticipant(other)
	conversation.setAttributes({
		welcomeMessages: ["No messages, chat to start the conversation!"]
	})
	conversations.push(conversation);
}
console.log(conversations);

const inbox = session.createInbox();
inbox.select();
inbox.mount(document.getElementById('talkjs-container'));

document.querySelectorAll('.message-friend').forEach(function (button){
	button.addEventListener('click', function (e){
		const friendId = parseInt(e.target.getAttribute("data-friend-id"));
		let friend;
		for (const other of others) {
			if (parseInt(other.id) === friendId) {
				friend = other;
			}
		}
		let conversation = session.getOrCreateConversation(
			Talk.oneOnOneId(me, friend)
		)
		conversation.setParticipant(me);
		conversation.setParticipant(friend);
		inbox.select(conversation)
	});

	let friendId = document.querySelector('#friendId');
	let friendUsername = document.querySelector('#friendUsername');
	let friendEmail = document.querySelector('#friendEmail');
	let friendPhoto = document.querySelector('#friendPhoto');
	if (friendId != null) {

		let friend = new Talk.User({
			id: friendId.textContent,
			name: friendUsername.textContent,
			email: friendEmail.textContent,
			photoUrl: friendPhoto.textContent,
		})

		let conversation = session.getOrCreateConversation(
			Talk.oneOnOneId(me, friend)
		)
		conversation.setParticipant(me);
		conversation.setParticipant(friend);
		inbox.select(conversation)

	}

})