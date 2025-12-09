import Hero from '../components/common/Hero';
import Features from '../components/common/Features';
import RoomTypes from '../components/common/RoomTypes';

const Home = () => {
  return (
    <div className="home-page">
      <Hero />
      <Features />
      <RoomTypes />
    </div>
  );
};

export default Home;
