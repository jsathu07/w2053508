import { useState } from "react";
import { useStompClient, useSubscription } from "react-stomp-hooks";

import TicketStatus from "./TicketStatus";
import ConfigurationForm from "./ConfigurationForm";

const ControlPanel = () => {
	const client = useStompClient();

	const [isStarted, setIsStarted] = useState(false);

	const toggleStatus = () => {
		if (client) {
			client.publish({
				destination: "/app/command",
				body: isStarted ? "stop" : "start",
			});
		} else {
			console.log("Error");
		}
	};

	useSubscription("/topic/status", (message) => {
		setIsStarted(parseInt(message.body, 10) === 1 ? true : false);
	});

	if (!isStarted) {
		return (
			<>
				<button onClick={toggleStatus}>Start</button>
				<ConfigurationForm client={client} />
			</>
		);
	} else {
		return (
			<>
				<TicketStatus />

				<button style={{ background: "red" }} onClick={toggleStatus}>
					Stop
				</button>
			</>
		);
	}
};

export default ControlPanel;
