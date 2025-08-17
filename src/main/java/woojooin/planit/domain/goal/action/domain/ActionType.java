package woojooin.planit.domain.goal.action.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActionType {

	ISA("ISA"),
	DEPOSIT("예적금"),
	;

	private final String description;

	public static ActionType fromCode(String actionType) {
		for (ActionType type : ActionType.values()) {
			if (type.getDescription().equals(actionType)) {
				return type;
			}
		}

		return null;
	}
}
