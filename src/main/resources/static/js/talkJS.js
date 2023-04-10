await Talk.ready;
const appId = await fetch("/keys").then(res => res.json()).then(res => res.talkJsAppId);
const loggedInUser = await fetch("/messages/user").then(res => res.json())
const me = new Talk.User({
	id: loggedInUser.id,
	name: loggedInUser.username,
	email: loggedInUser.email,
	photoUrl: 'https://talkjs.com/images/avatar-1.jpg',
	welcomeMessage: 'Hey there! How are you? :-)',
});
const session = new Talk.Session({
	appId: appId,
	me: me,
});
const other = new Talk.User({
	id: '22',
	name: 'Tigg',
	email: 'Sebastian@example.com',
	photoUrl: 'https://talkjs.com/images/avatar-5.jpg',
	welcomeMessage: 'Hey, how can I help?',
});

const conversation = session.getOrCreateConversation(
	Talk.oneOnOneId(me, other)
);
conversation.setParticipant(me);
conversation.setParticipant(other);

const inbox = session.createInbox();
inbox.select(conversation);
inbox.mount(document.getElementById('talkjs-container'));