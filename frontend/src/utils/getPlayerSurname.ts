export const getPlayerSurname = (fullName: string) => {
  return fullName.split(" ").slice(-1)[0];
};

export const getShortPlayerName = (fullName: string) => {
  if (!fullName) return "";
  const nameParts = fullName.split(" ");

  if (nameParts.length === 1) {
    return fullName;
  }

  const firstNameInitials = nameParts[0][0] + ".";
  const surname = nameParts.at(-1);

  return `${firstNameInitials} ${surname}`;
};
