select
    round
from
    GameList as round
    join fetch round.gameList game
    join fetch game.homeTeam
    join fetch game.guestTeam
    join fetch game.group
    join fetch game.tippList tipp
    join fetch tipp.user u
where
    round.id = :roundId
    and tipp.user.id = :userId
