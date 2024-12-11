import { useState } from "react";
import { useSubscription } from "react-stomp-hooks";

const TicketStatus = () => {
	const [data, setData] = useState("0");

	useSubscription("/topic/ticketsAvailable", (message) => setData(message.body));

	return <div className="ticket-box">Tickets available: {data}</div>;
};

export default TicketStatus;
