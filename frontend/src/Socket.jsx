import { useState } from "react";
import { StompSessionProvider, useStompClient, useSubscription } from "react-stomp-hooks";
import "./app.css";

const Socket = () => {
	return (
		<StompSessionProvider url={"http://localhost:8080/socket-endpoint"}>
			<ReceiveLogs />
			<Status />
		</StompSessionProvider>
	);
};

const ReceiveLogs = () => {
	const [log, setLog] = useState("No log has been received yet");

	useSubscription("/topic/logs", (message) => setLog(message.body));

	return <div className="log-box">System log: {log}</div>;
};

const TicketsAvailable = () => {
	const [data, setData] = useState("0");

	useSubscription("/topic/ticketsAvailable", (message) => setData(message.body));

	return <div className="ticket-box">Tickets available: {data}</div>;
};

const Status = () => {
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
		console.log(message.body, "do");
		setIsStarted(parseInt(message.body, 10) === 1 ? true : false);
	});

	if (!isStarted) {
		return (
			<>
				<button onClick={toggleStatus}>Start</button>
				<UpdateConfiguration client={client} />
			</>
		);
	} else {
		return (
			<>
				<TicketsAvailable />

				<button style={{ background: "red" }} onClick={toggleStatus}>
					Stop
				</button>
			</>
		);
	}
};

const UpdateConfiguration = ({ client }) => {
	const [totalTickets, setTotalTickets] = useState(15);
	const [ticketReleaseRate, setTicketReleaseRate] = useState(5);
	const [customerRetrievalRate, setCustomerRetrievalRate] = useState(2);
	const [maxTicketCapacity, setMaxTicketCapacity] = useState(5);

	const validate = (value) => {
		let num = parseInt(value);
		return !isNaN(num) && num > 0;
	};

	const handleSubmit = (e) => {
		e.preventDefault();

		if (
			!(
				validate(totalTickets) &&
				validate(ticketReleaseRate) &&
				validate(customerRetrievalRate) &&
				validate(maxTicketCapacity)
			)
		) {
			alert("Invalid configuration");
			return;
		}

		if (client) {
			const data = {
				totalTickets,
				ticketReleaseRate,
				customerRetrievalRate,
				maxTicketCapacity,
			};

			client.publish({
				destination: "/app/configure",
				body: JSON.stringify(data),
			});
		} else {
			console.log("Error");
		}
	};

	return (
		<>
			<form onSubmit={handleSubmit}>
				<label>Total Tickets:</label>
				<br />
				<input
					type="number"
					value={totalTickets}
					onChange={(e) => setTotalTickets(parseInt(e.target.value))}
					required
				/>
				<br />
				<label>Ticket Release Rate:</label>
				<br />
				<input
					type="number"
					value={ticketReleaseRate}
					onChange={(e) => setTicketReleaseRate(parseInt(e.target.value))}
					required
				/>
				<br />
				<label>Customer Retrieval Rate:</label>
				<input
					type="number"
					value={customerRetrievalRate}
					onChange={(e) => setCustomerRetrievalRate(parseInt(e.target.value))}
					required
				/>
				<br />
				<label>Max Ticket Capacity:</label>
				<br />
				<input
					type="number"
					value={maxTicketCapacity}
					onChange={(e) => setMaxTicketCapacity(parseInt(e.target.value))}
					required
				/>
				<br />
				<button style={{ background: "#008CBA" }} type="submit">
					Update Configuration
				</button>
			</form>
			<br />
		</>
	);
};

export default Socket;
