Talk.ready;
const appId = await fetch("/keys").then(res => res.json()).then(res => res.talkJsAppId);
const loggedInUser = await fetch("/loggedInUser").then(res => res.json());
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
const inbox = session.createInbox();
const friendId = document.querySelector('#friendId');
const friendUsername = document.querySelector('#friendUsername');
const friendEmail = document.querySelector('#friendEmail');
const friendPhoto = document.querySelector('#friendPhoto');

function createConversationWithFriend() {
	if (friendId === null) {
		return;
	}

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
	return conversation;
}

inbox.mount(document.getElementById('talkjs-container'));
inbox.select(createConversationWithFriend());