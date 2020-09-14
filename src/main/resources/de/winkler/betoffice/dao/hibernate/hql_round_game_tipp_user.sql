select
    tipp
from
    GameTipp as tipp
    join fetch tipp.game game
    join fetch game.group
    join fetch game.homeTeam
    join fetch game.guestTeam
    join fetch game.gameList round
    join fetch tipp.user user
where
    round.id = :roundId
    and tipp.user.id = :userId
