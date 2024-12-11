import { useState } from "react";

const ConfigurationForm = ({ client }) => {
	const [totalTickets, setTotalTickets] = useState(15);
	const [ticketReleaseRate, setTicketReleaseRate] = useState(5);
	const [customerRetrievalRate, setCustomerRetrievalRate] = useState(2);
	const [maxTicketCapacity, setMaxTicketCapacity] = useState(5);

	const validate = (value) => {
		return !isNaN(value) && value > 0;
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

export default ConfigurationForm;
